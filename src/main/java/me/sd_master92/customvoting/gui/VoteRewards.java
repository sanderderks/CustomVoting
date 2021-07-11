package me.sd_master92.customvoting.gui;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.Data;
import me.sd_master92.customvoting.constants.enumerations.SoundType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class VoteRewards extends GUI
{
    public static final String NAME = "Item Rewards";
    private final Main plugin;

    public VoteRewards(Main plugin)
    {
        super(NAME, 27);
        this.plugin = plugin;

        for (ItemStack reward : plugin.getData().getItems(Data.VOTE_REWARDS))
        {
            getInventory().addItem(reward);
        }
        getInventory().setItem(25, BACK_ITEM);
        getInventory().setItem(26, SAVE_ITEM);
    }

    public static void save(Main plugin, Player player, Inventory inv)
    {
        inv.setItem(25, null);
        inv.setItem(26, null);
        if (plugin.getData().setItems(Data.VOTE_REWARDS, inv.getContents()))
        {
            SoundType.SUCCESS.play(plugin, player);
            player.sendMessage(ChatColor.GREEN + "Successfully updated the Item Rewards!");
        } else
        {
            SoundType.FAILURE.play(plugin, player);
            player.sendMessage(ChatColor.RED + "Failed to update the Item Rewards!");
        }
    }
}
