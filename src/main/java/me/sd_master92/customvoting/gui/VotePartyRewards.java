package me.sd_master92.customvoting.gui;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.Data;
import me.sd_master92.customvoting.constants.enumerations.SoundType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class VotePartyRewards extends GUI
{
    public static final String NAME = "Vote Party Chest #";

    public VotePartyRewards(Main plugin, String key)
    {
        super(plugin, NAME + key, 54, true);
        getInventory().setContents(plugin.getData().getItems(Data.VOTE_PARTY + "." + key));
    }

    @Override
    public void onClick(InventoryClickEvent event, Player player, @NotNull ItemStack item)
    {

    }

    @Override
    public void onClose(InventoryCloseEvent event, Player player)
    {
        String key = getName().split("#")[1];
        if (plugin.getData().setItems(Data.VOTE_PARTY + "." + key, event.getInventory().getContents()))
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
