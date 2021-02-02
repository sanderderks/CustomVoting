package me.sd_master92.customvoting.listeners;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.Settings;
import me.sd_master92.customvoting.constants.enumerations.SoundType;
import me.sd_master92.customvoting.constants.enumerations.VotePartyType;
import me.sd_master92.customvoting.services.GUIService;
import me.sd_master92.customvoting.services.VotePartyService;
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
    private final GUIService guiService;
    private final VotePartyService votePartyService;
    private boolean cancelCloseEvent;

    public InventoryListener(Main plugin)
    {
        this.plugin = plugin;
        guiService = new GUIService(plugin);
        votePartyService = new VotePartyService(plugin);
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
                                SoundType.CLICK.play(plugin, player.getLocation());
                                cancelCloseEvent = true;
                                player.openInventory(guiService.getGeneralSettings());
                                break;
                            case DIAMOND:
                                SoundType.CLICK.play(plugin, player.getLocation());
                                cancelCloseEvent = true;
                                player.openInventory(guiService.getRewardSettings());
                                break;
                            case IRON_SHOVEL:
                                SoundType.NOT_ALLOWED.play(plugin, player.getLocation());
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
                        switch (item.getType())
                        {
                            case BARRIER:
                                SoundType.CLICK.play(plugin, player.getLocation());
                                cancelCloseEvent = true;
                                player.openInventory(guiService.getSettings());
                                break;
                            case CLOCK:
                                SoundType.CHANGE.play(plugin, player.getLocation());
                                plugin.getSettings().set(Settings.MONTHLY_RESET,
                                        !plugin.getSettings().getBoolean(Settings.MONTHLY_RESET));
                                plugin.getSettings().saveConfig();
                                event.setCurrentItem(guiService.getDoMonthlyResetSetting());
                                break;
                            case MUSIC_DISC_CAT:
                                SoundType.CHANGE.play(plugin, player.getLocation());
                                plugin.getSettings().set(Settings.USE_SOUND_EFFECTS,
                                        !plugin.getSettings().getBoolean(Settings.USE_SOUND_EFFECTS));
                                plugin.getSettings().saveConfig();
                                event.setCurrentItem(guiService.getUseSoundEffectsSetting());
                                break;
                            case FIREWORK_ROCKET:
                                SoundType.CHANGE.play(plugin, player.getLocation());
                                plugin.getSettings().set(Settings.FIREWORK,
                                        !plugin.getSettings().getBoolean(Settings.FIREWORK));
                                plugin.getSettings().saveConfig();
                                event.setCurrentItem(guiService.getUseFirework());
                                break;
                            case EXPERIENCE_BOTTLE:
                                SoundType.CHANGE.play(plugin, player.getLocation());
                                plugin.getSettings().set(Settings.VOTE_PARTY,
                                        !plugin.getSettings().getBoolean(Settings.VOTE_PARTY));
                                plugin.getSettings().saveConfig();
                                event.setCurrentItem(guiService.getDoVoteParty());
                                break;
                            case SPLASH_POTION:
                                SoundType.CHANGE.play(plugin, player.getLocation());
                                plugin.getSettings().setNumber(Settings.VOTE_PARTY_TYPE,
                                        VotePartyType.next(plugin).getValue());
                                event.setCurrentItem(guiService.getVotePartyType());
                                break;
                            case ENCHANTED_BOOK:
                                SoundType.CHANGE.play(plugin, player.getLocation());
                                if (plugin.getSettings().getNumber(Settings.VOTES_REQUIRED_FOR_VOTE_PARTY) < 100)
                                {
                                    plugin.getSettings().addNumber(Settings.VOTES_REQUIRED_FOR_VOTE_PARTY, 10);
                                } else
                                {
                                    plugin.getSettings().setNumber(Settings.VOTES_REQUIRED_FOR_VOTE_PARTY, 10);
                                }
                                event.setCurrentItem(guiService.getVotesUntilVoteParty());
                                break;
                            case ENDER_CHEST:
                                SoundType.CHANGE.play(plugin, player.getLocation());
                                if (plugin.getSettings().getNumber(Settings.VOTE_PARTY_COUNTDOWN) < 60)
                                {
                                    plugin.getSettings().addNumber(Settings.VOTE_PARTY_COUNTDOWN, 10);
                                } else
                                {
                                    plugin.getSettings().setNumber(Settings.VOTE_PARTY_COUNTDOWN, 0);
                                }
                                event.setCurrentItem(guiService.getVotePartyCountdownSetting());
                                break;
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
                            guiService.saveRewards(player, event.getInventory());
                        } else
                        {
                            SoundType.CLICK.play(plugin, player.getLocation());
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
                        SoundType.CLOSE.play(plugin, player.getLocation());
                        break;
                    case GUIService.REWARD_SETTINGS_INVENTORY:
                        guiService.saveRewards(player, event.getInventory());
                        break;
                    default:
                        if (event.getView().getTitle().contains(GUIService.VOTE_PARTY_REWARDS_INVENTORY))
                        {
                            votePartyService.saveRewards(player, event.getView().getTitle().split("#")[1],
                                    event.getInventory().getContents());
                        }
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
