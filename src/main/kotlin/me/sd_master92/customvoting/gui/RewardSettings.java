package me.sd_master92.customvoting.gui;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.Data;
import me.sd_master92.customvoting.constants.Settings;
import me.sd_master92.customvoting.constants.enumerations.SoundType;
import me.sd_master92.customvoting.listeners.PlayerListener;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RewardSettings extends GUI
{
    public static final String NAME = "Vote Rewards";

    public RewardSettings(Main plugin)
    {
        super(plugin, NAME, 9, true, true);

        getInventory().setItem(0, Data.getItemRewardSetting(plugin));
        getInventory().setItem(1, Settings.getMoneyRewardSetting(plugin));
        getInventory().setItem(2, Settings.getExperienceRewardSetting(plugin));
        getInventory().setItem(3, Data.getCommandRewardSetting(plugin));
        getInventory().setItem(4, Data.getLuckyRewardSetting(plugin));
        getInventory().setItem(5, Settings.getLuckyVoteChanceSetting(plugin));
        getInventory().setItem(8, BACK_ITEM);
    }

    @Override
    public void onClick(InventoryClickEvent event, Player player, @NotNull ItemStack item)
    {
        switch (item.getType())
        {
            case BARRIER:
                SoundType.CLICK.play(plugin, player);
                cancelCloseEvent();
                player.openInventory(new VoteSettings(plugin).getInventory());
                break;
            case CHEST:
                SoundType.CLICK.play(plugin, player);
                cancelCloseEvent();
                player.openInventory(new ItemRewards(plugin).getInventory());
                break;
            case GOLD_INGOT:
                if (Main.economy != null)
                {
                    SoundType.CHANGE.play(plugin, player);
                    PlayerListener.moneyInput.add(player.getUniqueId());
                    cancelCloseEvent();
                    player.closeInventory();
                    player.sendMessage(ChatColor.GREEN + "Please enter a number between 0 and 1.000" +
                            ".000");
                    player.sendMessage(ChatColor.GRAY + "Type 'cancel' to go back");
                    new BukkitRunnable()
                    {
                        @Override
                        public void run()
                        {
                            if (!PlayerListener.moneyInput.contains(player.getUniqueId()))
                            {
                                player.openInventory(new RewardSettings(plugin).getInventory());
                                cancelCloseEvent();
                                cancel();
                            } else if (!player.isOnline())
                            {
                                PlayerListener.moneyInput.remove(player.getUniqueId());
                                cancel();
                            }
                        }
                    }.runTaskTimer(plugin, 0, 10);
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
                event.setCurrentItem(Settings.getExperienceRewardSetting(plugin));
                break;
            case COMMAND_BLOCK:
                SoundType.CHANGE.play(plugin, player);
                PlayerListener.commandInput.add(player.getUniqueId());
                cancelCloseEvent();
                player.closeInventory();
                player.sendMessage(ChatColor.GREEN + "Please enter a command to add or remove from " +
                        "the list");
                player.sendMessage(ChatColor.GREEN + "(with %PLAYER% as placeholder)");
                player.sendMessage(ChatColor.GRAY + "Type 'cancel' to go back");
                player.sendMessage("");
                List<String> commands = plugin.getData().getStringList(Data.VOTE_COMMANDS);
                if (commands.isEmpty())
                {
                    player.sendMessage(ChatColor.RED + "There are currently no commands.");
                } else
                {
                    player.sendMessage(ChatColor.GRAY + "Commands:");
                    for (String command : commands)
                    {
                        player.sendMessage(ChatColor.GRAY + "/" + ChatColor.GREEN + command);
                    }
                }
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        if (!PlayerListener.commandInput.contains(player.getUniqueId()))
                        {
                            player.openInventory(new RewardSettings(plugin).getInventory());
                            cancelCloseEvent();
                            cancel();
                        } else if (!player.isOnline())
                        {
                            PlayerListener.commandInput.remove(player.getUniqueId());
                            cancel();
                        }
                    }
                }.runTaskTimer(plugin, 0, 10);
                break;
            case ENDER_CHEST:
                SoundType.CLICK.play(plugin, player);
                cancelCloseEvent();
                player.openInventory(new LuckyRewards(plugin).getInventory());
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
                event.setCurrentItem(Settings.getLuckyVoteChanceSetting(plugin));
                break;
        }
    }

    @Override
    public void onClose(InventoryCloseEvent event, Player player)
    {
        SoundType.CLOSE.play(plugin, player);
    }
}
