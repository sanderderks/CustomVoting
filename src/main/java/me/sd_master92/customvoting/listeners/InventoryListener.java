package me.sd_master92.customvoting.listeners;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.Data;
import me.sd_master92.customvoting.constants.Settings;
import me.sd_master92.customvoting.constants.enumerations.SoundType;
import me.sd_master92.customvoting.constants.enumerations.VotePartyType;
import me.sd_master92.customvoting.services.GUIService;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InventoryListener implements Listener
{
    private final Main plugin;
    private final GUIService guiService;
    private final List<UUID> cancelCloseEvent;

    public InventoryListener(Main plugin)
    {
        this.plugin = plugin;
        guiService = new GUIService(plugin);
        cancelCloseEvent = new ArrayList<>();
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
                    event.setCancelled(true);
                    if (item != null)
                    {
                        switch (item.getType())
                        {
                            case COMMAND_BLOCK:
                                SoundType.CLICK.play(plugin, player);
                                cancelCloseEvent.add(player.getUniqueId());
                                player.openInventory(guiService.getGeneralSettings());
                                break;
                            case DIAMOND:
                                SoundType.CLICK.play(plugin, player);
                                cancelCloseEvent.add(player.getUniqueId());
                                player.openInventory(guiService.getRewardSettings());
                                break;
                            case IRON_SHOVEL:
                                SoundType.NOT_ALLOWED.play(plugin, player);
                                break;
                        }
                    }
                    break;
                case GUIService.GENERAL_SETTINGS_INVENTORY:
                    event.setCancelled(true);
                    if (item != null)
                    {
                        switch (item.getType())
                        {
                            case BARRIER:
                                SoundType.CLICK.play(plugin, player);
                                cancelCloseEvent.add(player.getUniqueId());
                                player.openInventory(guiService.getConfig());
                                break;
                            case CLOCK:
                                SoundType.CHANGE.play(plugin, player);
                                plugin.getConfig().set(Settings.MONTHLY_RESET,
                                        !plugin.getConfig().getBoolean(Settings.MONTHLY_RESET));
                                plugin.getConfig().saveConfig();
                                event.setCurrentItem(guiService.getDoMonthlyResetSetting());
                                break;
                            case MUSIC_DISC_CAT:
                                SoundType.CHANGE.play(plugin, player);
                                plugin.getConfig().set(Settings.USE_SOUND_EFFECTS,
                                        !plugin.getConfig().getBoolean(Settings.USE_SOUND_EFFECTS));
                                plugin.getConfig().saveConfig();
                                event.setCurrentItem(guiService.getUseSoundEffectsSetting());
                                break;
                            case FIREWORK_ROCKET:
                                SoundType.CHANGE.play(plugin, player);
                                plugin.getConfig().set(Settings.FIREWORK,
                                        !plugin.getConfig().getBoolean(Settings.FIREWORK));
                                plugin.getConfig().saveConfig();
                                event.setCurrentItem(guiService.getUseFireworkSetting());
                                break;
                            case EXPERIENCE_BOTTLE:
                                SoundType.CHANGE.play(plugin, player);
                                plugin.getConfig().set(Settings.VOTE_PARTY,
                                        !plugin.getConfig().getBoolean(Settings.VOTE_PARTY));
                                plugin.getConfig().saveConfig();
                                event.setCurrentItem(guiService.getDoVotePartySetting());
                                break;
                            case SPLASH_POTION:
                                SoundType.CHANGE.play(plugin, player);
                                plugin.getConfig().setNumber(Settings.VOTE_PARTY_TYPE,
                                        VotePartyType.next(plugin).getValue());
                                event.setCurrentItem(guiService.getVotePartyTypeSetting());
                                break;
                            case ENCHANTED_BOOK:
                                SoundType.CHANGE.play(plugin, player);
                                if (plugin.getConfig().getNumber(Settings.VOTES_REQUIRED_FOR_VOTE_PARTY) < 100)
                                {
                                    plugin.getConfig().addNumber(Settings.VOTES_REQUIRED_FOR_VOTE_PARTY, 10);
                                } else
                                {
                                    plugin.getConfig().setNumber(Settings.VOTES_REQUIRED_FOR_VOTE_PARTY, 10);
                                }
                                event.setCurrentItem(guiService.getVotesUntilVotePartySetting());
                                break;
                            case ENDER_CHEST:
                                SoundType.CHANGE.play(plugin, player);
                                if (plugin.getConfig().getNumber(Settings.VOTE_PARTY_COUNTDOWN) < 60)
                                {
                                    plugin.getConfig().addNumber(Settings.VOTE_PARTY_COUNTDOWN, 10);
                                } else
                                {
                                    plugin.getConfig().setNumber(Settings.VOTE_PARTY_COUNTDOWN, 0);
                                }
                                event.setCurrentItem(guiService.getVotePartyCountdownSetting());
                                break;
                            case TOTEM_OF_UNDYING:
                                SoundType.CHANGE.play(plugin, player);
                                plugin.getConfig().set(Settings.LUCKY_VOTE,
                                        !plugin.getConfig().getBoolean(Settings.LUCKY_VOTE));
                                event.setCurrentItem(guiService.getDoLuckyVoteSetting());
                                break;
                        }
                    }
                    break;
                case GUIService.REWARD_SETTINGS_INVENTORY:
                    event.setCancelled(true);
                    if (item != null)
                    {
                        switch (item.getType())
                        {
                            case BARRIER:
                                SoundType.CLICK.play(plugin, player);
                                cancelCloseEvent.add(player.getUniqueId());
                                player.openInventory(guiService.getConfig());
                                break;
                            case CHEST:
                                SoundType.CLICK.play(plugin, player);
                                cancelCloseEvent.add(player.getUniqueId());
                                player.openInventory(guiService.getItemRewards());
                                break;
                            case GOLD_INGOT:
                                if (Main.economy != null)
                                {
                                    SoundType.CHANGE.play(plugin, player);
                                    PlayerListener.chatInput.add(player.getUniqueId());
                                    cancelCloseEvent.add(player.getUniqueId());
                                    player.closeInventory();
                                    player.sendMessage(ChatColor.GREEN + "Please enter a number between 0 and 1.000" +
                                            ".000");
                                    player.sendMessage(ChatColor.GRAY + "Type 'cancel' to go back");
                                    new BukkitRunnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            if (!PlayerListener.chatInput.contains(player.getUniqueId()))
                                            {
                                                player.openInventory(guiService.getRewardSettings());
                                                cancelCloseEvent.remove(player.getUniqueId());
                                                cancel();
                                            } else if (!player.isOnline())
                                            {
                                                PlayerListener.chatInput.remove(player.getUniqueId());
                                                cancel();
                                            }
                                        }
                                    }.runTaskTimer(plugin, 0, 10);
                                    break;
                                } else
                                {
                                    SoundType.FAILURE.play(plugin, player);
                                }
                                break;
                            case EXPERIENCE_BOTTLE:
                                SoundType.CHANGE.play(plugin, player);
                                if (plugin.getConfig().getNumber(Settings.VOTE_REWARD_EXPERIENCE) < 10)
                                {
                                    plugin.getConfig().addNumber(Settings.VOTE_REWARD_EXPERIENCE, 1);
                                } else
                                {
                                    plugin.getConfig().setNumber(Settings.VOTE_REWARD_EXPERIENCE, 0);
                                }
                                event.setCurrentItem(guiService.getExperienceRewardSetting());
                                break;
                            case ENDER_CHEST:
                                SoundType.CLICK.play(plugin, player);
                                cancelCloseEvent.add(player.getUniqueId());
                                player.openInventory(guiService.getLuckyRewards());
                                break;
                            case ENDER_EYE:
                                SoundType.CHANGE.play(plugin, player);
                                int chance = plugin.getConfig().getNumber(Settings.LUCKY_VOTE_CHANCE);
                                if (chance < 10)
                                {
                                    plugin.getConfig().addNumber(Settings.LUCKY_VOTE_CHANCE, 1);
                                } else if (chance < 100)
                                {
                                    plugin.getConfig().addNumber(Settings.LUCKY_VOTE_CHANCE, 5);
                                } else
                                {
                                    plugin.getConfig().setNumber(Settings.LUCKY_VOTE_CHANCE, 1);
                                }
                                event.setCurrentItem(guiService.getLuckyVoteChanceSetting());
                                break;
                        }
                    }
                    break;
                case GUIService
                        .ITEM_REWARDS_INVENTORY:
                    if (event.getSlot() >= 25)
                    {
                        event.setCancelled(true);
                        if (event.getSlot() == 26)
                        {
                            guiService.saveRewards(Data.VOTE_REWARDS, player, event.getInventory());
                        } else
                        {
                            SoundType.CLICK.play(plugin, player);
                        }
                        cancelCloseEvent.add(player.getUniqueId());
                        player.openInventory(guiService.getRewardSettings());
                    }
                    break;
                case GUIService
                        .LUCKY_REWARDS_INVENTORY:
                    if (event.getSlot() >= 25)
                    {
                        event.setCancelled(true);
                        if (event.getSlot() == 26)
                        {
                            guiService.saveRewards(Data.LUCKY_REWARDS, player, event.getInventory());
                        } else
                        {
                            SoundType.CLICK.play(plugin, player);
                        }
                        cancelCloseEvent.add(player.getUniqueId());
                        player.openInventory(guiService.getRewardSettings());
                    }
                    break;
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event)
    {
        if (event.getPlayer() instanceof Player)
        {
            Player player = (Player) event.getPlayer();
            if (!cancelCloseEvent.contains(player.getUniqueId()))
            {
                String title = event.getView().getTitle();
                switch (title)
                {
                    case GUIService.MAIN_SETTINGS_INVENTORY:
                    case GUIService.GENERAL_SETTINGS_INVENTORY:
                    case GUIService.REWARD_SETTINGS_INVENTORY:
                        SoundType.CLOSE.play(plugin, player);
                        break;
                    case GUIService.ITEM_REWARDS_INVENTORY:
                        guiService.saveRewards(Data.VOTE_REWARDS, player, event.getInventory());
                        break;
                    case GUIService.LUCKY_REWARDS_INVENTORY:
                        guiService.saveRewards(Data.LUCKY_REWARDS, player, event.getInventory());
                        break;
                }
                if (title.contains(GUIService.VOTE_PARTY_REWARDS_INVENTORY))
                {
                    String key = title.split("#")[1];
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
            } else
            {
                cancelCloseEvent.remove(player.getUniqueId());
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
