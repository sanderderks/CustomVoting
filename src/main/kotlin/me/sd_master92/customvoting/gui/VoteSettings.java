package me.sd_master92.customvoting.gui;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.enumerations.SoundType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class VoteSettings extends GUI
{
    public static final String NAME = "Vote Settings";
    public static final ItemStack GENERAL_SETTINGS = createItem(Material.COMMAND_BLOCK, ChatColor.AQUA + "General",
            null, true);
    public static final ItemStack REWARD_SETTINGS = createItem(Material.DIAMOND, ChatColor.LIGHT_PURPLE + "Rewards",
            null, true);
    public static final ItemStack MESSAGES = createItem(Material.WRITABLE_BOOK, ChatColor.YELLOW + "Messages",
            null, true);
    public static final ItemStack SUPPORT = createItem(Material.SPYGLASS, ChatColor.GREEN + "Support",
            null, true);

    public VoteSettings(Main plugin)
    {
        super(plugin, NAME, 9, false, true);

        getInventory().setItem(1, GENERAL_SETTINGS);
        getInventory().setItem(3, REWARD_SETTINGS);
        getInventory().setItem(5, MESSAGES);
        getInventory().setItem(7, SUPPORT);
    }

    @Override
    public void onClick(InventoryClickEvent event, Player player, @NotNull ItemStack item)
    {
        switch (item.getType())
        {
            case COMMAND_BLOCK:
                SoundType.CLICK.play(plugin, player);
                cancelCloseEvent();
                player.openInventory(new GeneralSettings(plugin).getInventory());
                break;
            case DIAMOND:
                SoundType.CLICK.play(plugin, player);
                cancelCloseEvent();
                player.openInventory(new RewardSettings(plugin).getInventory());
                break;
            case WRITABLE_BOOK:
                SoundType.CLICK.play(plugin, player);
                cancelCloseEvent();
                player.openInventory(new MessageSettings(plugin).getInventory());
                break;
            case SPYGLASS:
                SoundType.CLICK.play(plugin, player);
                cancelCloseEvent();
                player.openInventory(new Support(plugin).getInventory());
                break;
        }
    }

    @Override
    public void onClose(InventoryCloseEvent event, Player player)
    {
        SoundType.CLOSE.play(plugin, player);
    }
}
