package me.sd_master92.customvoting.listeners;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.Names;
import me.sd_master92.customvoting.constants.types.SoundType;
import me.sd_master92.customvoting.helpers.SoundHelper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener
{
    private final Main plugin;
    private boolean cancelCloseEvent;

    public InventoryListener(Main plugin)
    {
        this.plugin = plugin;
        cancelCloseEvent = false;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        if (event.getWhoClicked() instanceof Player)
        {
            Player player = (Player) event.getWhoClicked();
            ItemStack item = event.getCurrentItem();
            switch (event.getView().getTitle())
            {
                case Names.MAIN_SETTINGS_INVENTORY:
                {
                    event.setCancelled(true);
                    if (item != null)
                    {
                        switch (item.getType())
                        {
                            case COMMAND_BLOCK:
                                SoundHelper.playSound(SoundType.CLICK, player.getLocation());
                                cancelCloseEvent = true;
                                player.openInventory(plugin.getGuiService().getGeneralSettings());
                                break;
                            case DIAMOND:
                                SoundHelper.playSound(SoundType.CLICK, player.getLocation());
                                cancelCloseEvent = true;
                                player.openInventory(plugin.getGuiService().getRewardSettings());
                                break;
                            case IRON_SHOVEL:
                                SoundHelper.playSound(SoundType.NOT_ALLOWED, player.getLocation());
                                break;
                        }
                    }
                }
                break;
                case Names.GENERAL_SETTINGS_INVENTORY:
                {
                    event.setCancelled(true);
                    if (item != null)
                    {
                        if (item.getType() == Material.BARRIER)
                        {
                            SoundHelper.playSound(SoundType.CLICK, player.getLocation());
                            cancelCloseEvent = true;
                            player.openInventory(plugin.getGuiService().getSettings());
                        }
                    }
                }
                break;
                case Names
                        .REWARD_SETTINGS_INVENTORY:
                {
                    if (event.getSlot() >= 25)
                    {
                        event.setCancelled(true);
                        if (event.getSlot() == 26)
                        {
                            plugin.getGuiService().saveRewardSettings(player, event.getInventory());
                        } else
                        {
                            SoundHelper.playSound(SoundType.CLICK, player.getLocation());
                        }
                        cancelCloseEvent = true;
                        player.openInventory(plugin.getGuiService().getSettings());
                    }
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event)
    {
        if (event.getPlayer() instanceof Player)
        {
            if (cancelCloseEvent)
            {
                cancelCloseEvent = false;
            } else
            {
                Player player = (Player) event.getPlayer();
                switch (event.getView().getTitle())
                {
                    case Names.MAIN_SETTINGS_INVENTORY:
                    case Names.GENERAL_SETTINGS_INVENTORY:
                        SoundHelper.playSound(SoundType.CLOSE, player.getLocation());
                        break;
                    case Names.REWARD_SETTINGS_INVENTORY:
                        plugin.getGuiService().saveRewardSettings(player, event.getInventory());
                }
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event)
    {
        switch (event.getView().getTitle())
        {
            case Names.MAIN_SETTINGS_INVENTORY:
            case Names.GENERAL_SETTINGS_INVENTORY:
                event.setCancelled(true);
                break;
            default:
                event.setCancelled(false);
        }
    }
}
