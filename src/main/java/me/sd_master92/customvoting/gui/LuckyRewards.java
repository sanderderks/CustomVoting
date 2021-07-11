package me.sd_master92.customvoting.gui;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.Data;
import me.sd_master92.customvoting.constants.enumerations.SoundType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class LuckyRewards extends GUI
{
    public static final String NAME = "Lucky Rewards";
    private final Main plugin;

    public LuckyRewards(Main plugin)
    {
        super(NAME, 27);
        this.plugin = plugin;

        for (ItemStack reward : plugin.getData().getItems(Data.LUCKY_REWARDS))
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
        if (plugin.getData().setItems(Data.LUCKY_REWARDS, inv.getContents()))
        {
            SoundType.SUCCESS.play(plugin, player);
            player.sendMessage(ChatColor.GREEN + "Successfully updated the Lucky Rewards!");
        } else
        {
            SoundType.FAILURE.play(plugin, player);
            player.sendMessage(ChatColor.RED + "Failed to update the Lucky Rewards!");
        }
    }
}
