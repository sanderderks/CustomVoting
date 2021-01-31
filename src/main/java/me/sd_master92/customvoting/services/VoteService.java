package me.sd_master92.customvoting.services;

import com.vexsoftware.votifier.model.Vote;
import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.VoteFile;
import me.sd_master92.customvoting.constants.types.Messages;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class VoteService
{
    private final Main plugin;

    public VoteService(Main plugin)
    {
        this.plugin = plugin;
    }

    public void forwardVote(Player player, Vote vote)
    {
        new VoteFile(player.getUniqueId().toString(), plugin).addVote(true);
        broadcastVote(vote);
        shootFirework(player.getLocation());
        giveRewards(player);
    }

    public void broadcastVote(Vote vote)
    {
        HashMap<String, String> placeholders = new HashMap<>();
        placeholders.put("%PLAYER%", vote.getUsername());
        placeholders.put("%SERVICE%", vote.getServiceName());
        String message = plugin.getMessages().getMessage(Messages.BROADCAST, placeholders);
        plugin.getServer().broadcastMessage(message);
    }

    public void shootFirework(Location loc)
    {
        World world = loc.getWorld();
        if (world != null)
        {
            world.spawnEntity(loc, EntityType.FIREWORK);
        }
    }

    public void giveRewards(Player player)
    {
        for (ItemStack reward : plugin.getData().getItems("rewards"))
        {
            for(ItemStack item : player.getInventory().addItem(reward).values())
            {
                player.getWorld().dropItemNaturally(player.getLocation(), item);
            }
        }
    }
}
