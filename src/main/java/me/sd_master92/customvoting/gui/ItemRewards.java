package me.sd_master92.customvoting.gui;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.Data;
import me.sd_master92.customvoting.constants.enumerations.SoundType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemRewards extends GUI
{
    public static final String NAME = "Item Rewards";

    public ItemRewards(Main plugin)
    {
        super(plugin, NAME, 27, true);

        for (ItemStack reward : plugin.getData().getItems(Data.ITEM_REWARDS))
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
        if (plugin.getData().setItems(Data.ITEM_REWARDS, inv.getContents()))
        {
            SoundType.SUCCESS.play(plugin, player);
            player.sendMessage(ChatColor.GREEN + "Successfully updated the Item Rewards!");
        } else
        {
            SoundType.FAILURE.play(plugin, player);
            player.sendMessage(ChatColor.RED + "Failed to update the Item Rewards!");
        }
    }

    @Override
    public void onClick(InventoryClickEvent event, Player player, @NotNull ItemStack item)
    {
        if (event.getSlot() >= 25)
        {
            event.setCancelled(true);
            if (event.getSlot() == 26)
            {
                ItemRewards.save(plugin, player, event.getInventory());
            } else
            {
                SoundType.CLICK.play(plugin, player);
            }
            cancelCloseEvent();
            player.openInventory(new RewardSettings(plugin).getInventory());
        }
    }

    @Override
    public void onClose(InventoryCloseEvent event, Player player)
    {
        ItemRewards.save(plugin, player, event.getInventory());
    }
}
