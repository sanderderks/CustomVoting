package me.sd_master92.customvoting.gui;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.Settings;
import me.sd_master92.customvoting.constants.enumerations.SoundType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MessageSettings extends GUI
{
    public static final ItemStack VOTE_LINKS = createItem(Material.SOUL_TORCH, ChatColor.LIGHT_PURPLE + "Vote Links",
            ChatColor.GRAY + "Place items in this inventory;;" + ChatColor.GRAY + "Right-click to edit an item");

    public MessageSettings(Main plugin)
    {
        super(plugin, "Message Settings", 9, false, true);

        getInventory().setItem(0, VOTE_LINKS);
        getInventory().setItem(8, BACK_ITEM);
        getInventory().setItem(1, Settings.getUseVoteLinksInventory(plugin));
    }

    @Override
    public void onClick(InventoryClickEvent event, Player player, @NotNull ItemStack item)
    {
        switch (item.getType())
        {
            case SOUL_TORCH:
                SoundType.CLICK.play(plugin, player);
                cancelCloseEvent();
                player.openInventory(new VoteLinks(plugin).getInventory());
                break;
            case CHEST:
                SoundType.CHANGE.play(plugin, player);
                plugin.getConfig().set(Settings.VOTE_LINK_INVENTORY, !plugin.getConfig().getBoolean(Settings.VOTE_LINK_INVENTORY));
                plugin.getConfig().saveConfig();
                event.setCurrentItem(Settings.getUseVoteLinksInventory(plugin));
                break;
            case BARRIER:
                SoundType.CLICK.play(plugin, player);
                cancelCloseEvent();
                player.openInventory(new VoteSettings(plugin).getInventory());
                break;
        }
    }

    @Override
    public void onClose(InventoryCloseEvent event, Player player)
    {
        SoundType.CLOSE.play(plugin, player);
    }
}
