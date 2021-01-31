package me.sd_master92.customvoting.services;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import me.sd_master92.customfile.PlayerFile;
import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.VoteFile;
import me.sd_master92.customvoting.constants.Messages;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class VoteService
{
    private final Main plugin;

    public VoteService(Main plugin)
    {
        this.plugin = plugin;
    }

    public void fakeVote(String name, String service)
    {
        Vote vote = new Vote();
        vote.setUsername(name);
        vote.setServiceName(service);
        vote.setAddress("0.0.0.0");
        Date date = new Date();
        vote.setTimeStamp(String.valueOf(date.getTime()));
        plugin.getServer().getPluginManager().callEvent(new VotifierEvent(vote));
    }

    public void fakeVote(String name)
    {
        fakeVote(name, "fakevote.com");
    }

    public void queueVote(Vote vote)
    {
        PlayerFile playerFile = PlayerFile.getByName(vote.getUsername(), plugin);
        if(playerFile != null)
        {
            String path = "queue." + playerFile.getUuid();
            List<String> queue = plugin.getData().getStringList(path);
            queue.add(vote.getServiceName());
            plugin.getData().set(path, queue);
            plugin.getData().saveConfig();
        }
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
