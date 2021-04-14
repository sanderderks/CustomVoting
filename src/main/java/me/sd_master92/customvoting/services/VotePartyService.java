package me.sd_master92.customvoting.services;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.Data;
import me.sd_master92.customvoting.constants.enumerations.SoundType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class VotePartyService
{
    public static final ItemStack VOTE_PARTY_ITEM = GUIService.createItem(Material.ENDER_CHEST,
            ChatColor.LIGHT_PURPLE +
                    "Vote Party Chest",
            ChatColor.GRAY + "Place this chest somewhere in the sky.;" + ChatColor.GRAY + "The contents of this chest" +
                    " will;" +
                    ChatColor.GRAY + "start dropping when the voteparty starts.");
    private final Main plugin;

    public VotePartyService(Main plugin)
    {
        this.plugin = plugin;
    }

    public void saveRewards(Player player, String key, ItemStack[] contents)
    {
        if (plugin.getData().setItems(Data.VOTE_PARTY + "." + key, contents))
        {
            SoundType.SUCCESS.play(plugin, player);
            player.sendMessage(ChatColor.GREEN + "Successfully updated Vote Party Chest #" + key);
        } else
        {
            SoundType.FAILURE.play(plugin, player);
            player.sendMessage(ChatColor.RED + "Failed to update Vote Party Chest #" + key);
        }
    }
}
