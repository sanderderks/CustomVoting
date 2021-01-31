package me.sd_master92.customvoting.listeners;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.InventoryName;
import me.sd_master92.customvoting.constants.Sounds;
import me.sd_master92.customvoting.services.GUIService;
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
    private final GUIService guiService;
    private boolean cancelCloseEvent;

    public InventoryListener(Main plugin)
    {
        guiService = new GUIService(plugin);
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
                case GUIService.MAIN_SETTINGS_INVENTORY:
                {
                    event.setCancelled(true);
                    if (item != null)
                    {
                        switch (item.getType())
                        {
                            case COMMAND_BLOCK:
                                Sounds.CLICK.play(player.getLocation());
                                cancelCloseEvent = true;
                                player.openInventory(guiService.getGeneralSettings());
                                break;
                            case DIAMOND:
                                Sounds.CLICK.play(player.getLocation());
                                cancelCloseEvent = true;
                                player.openInventory(guiService.getRewardSettings());
                                break;
                            case IRON_SHOVEL:
                                Sounds.NOT_ALLOWED.play(player.getLocation());
                                break;
                        }
                    }
                }
                break;
                case GUIService.GENERAL_SETTINGS_INVENTORY:
                {
                    event.setCancelled(true);
                    if (item != null)
                    {
                        if (item.getType() == Material.BARRIER)
                        {
                            Sounds.CLICK.play(player.getLocation());
                            cancelCloseEvent = true;
                            player.openInventory(guiService.getSettings());
                        }
                    }
                }
                break;
                case GUIService
                        .REWARD_SETTINGS_INVENTORY:
                {
                    if (event.getSlot() >= 25)
                    {
                        event.setCancelled(true);
                        if (event.getSlot() == 26)
                        {
                            guiService.saveRewardSettings(player, event.getInventory());
                        } else
                        {
                            Sounds.CLICK.play(player.getLocation());
                        }
                        cancelCloseEvent = true;
                        player.openInventory(guiService.getSettings());
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
                    case GUIService.MAIN_SETTINGS_INVENTORY:
                    case GUIService.GENERAL_SETTINGS_INVENTORY:
                        Sounds.CLOSE.play(player.getLocation());
                        break;
                    case GUIService.REWARD_SETTINGS_INVENTORY:
                        guiService.saveRewardSettings(player, event.getInventory());
                }
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event)
    {
        switch (event.getView().getTitle())
        {
            case GUIService.MAIN_SETTINGS_INVENTORY:
            case GUIService.GENERAL_SETTINGS_INVENTORY:
                event.setCancelled(true);
                break;
            default:
                event.setCancelled(false);
        }
    }
}
