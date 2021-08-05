package me.sd_master92.customvoting.gui;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.Data;
import me.sd_master92.customvoting.constants.enumerations.SoundType;
import me.sd_master92.customvoting.listeners.PlayerListener;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class VoteLinks extends GUI
{
    private final boolean isPublic;

    public VoteLinks(Main plugin, boolean isPublic)
    {
        super(plugin, "Vote Links", 27, true);
        this.isPublic = isPublic;

        ItemStack[] items = plugin.getData().getItems(Data.VOTE_LINK_ITEMS);
        for (int i = 0; i < items.length; i++)
        {
            getInventory().setItem(i, items[i]);
        }
    }

    public VoteLinks(Main plugin)
    {
        this(plugin, false);
    }

    public static void save(Main plugin, Player player, ItemStack[] items, boolean notify)
    {
        if (plugin.getData().setItemsWithNull(Data.VOTE_LINK_ITEMS, items))
        {
            if (notify)
            {
                SoundType.SUCCESS.play(plugin, player);
                player.sendMessage(ChatColor.GREEN + "Successfully updated the Vote Links!");
            }
        } else
        {
            SoundType.FAILURE.play(plugin, player);
            player.sendMessage(ChatColor.RED + "Failed to update the Vote Links!");
        }
    }

    @Override
    public void onClick(InventoryClickEvent event, Player player, @NotNull ItemStack item)
    {
        if (!isPublic)
        {
            if (event.getClick() == ClickType.RIGHT)
            {
                save(plugin, player, getInventory().getContents(), false);
                PlayerListener.voteLinkInput.put(player.getUniqueId(), event.getSlot());
                cancelCloseEvent();
                player.closeInventory();
                player.sendMessage(ChatColor.GREEN + "Enter a title for this item (with & colors)", ChatColor.GRAY +
                        "Type 'cancel' to continue");
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        if (!PlayerListener.voteLinkInput.containsKey(player.getUniqueId()))
                        {
                            player.openInventory(new VoteLinks(plugin).getInventory());
                            cancelCloseEvent();
                            cancel();
                        } else if (!player.isOnline())
                        {
                            PlayerListener.voteLinkInput.remove(player.getUniqueId());
                            cancel();
                        }
                    }
                }.runTaskTimer(plugin, 0, 10);
            }
        } else
        {
            event.setCancelled(true);
            String voteLink = plugin.getData().getMessage(Data.VOTE_LINKS + "." + event.getSlot());
            if (!voteLink.isEmpty())
            {
                SoundType.SUCCESS.play(plugin, player);
                player.closeInventory();
                player.sendMessage(voteLink);
            }
        }
    }

    @Override
    public void onClose(InventoryCloseEvent event, Player player)
    {
        if (!isPublic)
        {
            save(plugin, player, getInventory().getContents(), true);
        } else
        {
            SoundType.CLOSE.play(plugin, player);
        }
    }
}
