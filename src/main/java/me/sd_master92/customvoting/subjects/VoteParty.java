package me.sd_master92.customvoting.subjects;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.Data;
import me.sd_master92.customvoting.constants.Messages;
import me.sd_master92.customvoting.constants.Settings;
import me.sd_master92.customvoting.constants.enumerations.SoundType;
import me.sd_master92.customvoting.constants.enumerations.VotePartyType;
import me.sd_master92.customvoting.gui.GUI;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class VoteParty
{
    public static final ItemStack VOTE_PARTY_ITEM = GUI.createItem(Material.ENDER_CHEST,
            ChatColor.LIGHT_PURPLE +
                    "Vote Party Chest",
            ChatColor.GRAY + "Place this chest somewhere in the sky.;" + ChatColor.GRAY + "The contents of this chest" +
                    " will;" +
                    ChatColor.GRAY + "start dropping when the voteparty starts.");
    private static final List<VoteParty> queue = new ArrayList<>();
    private static boolean isActive = false;
    private final Main plugin;
    private final int votePartyType;
    private int count;

    public VoteParty(Main plugin)
    {
        this.plugin = plugin;
        votePartyType = plugin.getConfig().getNumber(Settings.VOTE_PARTY_TYPE);
        count = plugin.getConfig().getNumber(Settings.VOTE_PARTY_COUNTDOWN);
    }

    public void start()
    {
        queue.add(this);
        if (!isActive)
        {
            isActive = true;
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    switch (count)
                    {
                        case 30:
                        case 15:
                        case 10:
                            Map<String, String> placeholders = new HashMap<>();
                            placeholders.put("%TIME%", "" + count);
                            SoundType.NOTIFY.playForAll(plugin);
                            plugin.getServer().broadcastMessage(Messages.VOTE_PARTY_COUNTDOWN.getMessage(plugin, placeholders));
                            break;
                        case 5:
                        case 4:
                        case 3:
                        case 2:
                        case 1:
                            placeholders = new HashMap<>();
                            placeholders.put("%TIME%", "" + count);
                            placeholders.put("%s%", count == 1 ? "" : "s");
                            SoundType.NOTIFY.playForAll(plugin);
                            plugin.getServer().broadcastMessage(Messages.VOTE_PARTY_COUNTDOWN_ENDING.getMessage(plugin, placeholders));
                            break;
                        case 0:
                            SoundType.VOTE_PARTY_START.playForAll(plugin);
                            plugin.getServer().broadcastMessage(Messages.VOTE_PARTY_START.getMessage(plugin));
                            if (votePartyType == VotePartyType.RANDOM_CHEST_AT_A_TIME.getValue())
                            {
                                dropChestsRandomly();
                            } else if (votePartyType == VotePartyType.ADD_TO_INVENTORY.getValue())
                            {
                                addToInventory();
                            } else
                            {
                                dropChests();
                            }
                            cancel();
                    }
                    count--;
                }
            }.runTaskTimer(plugin, 0, 20);
        }
    }

    private void stop()
    {
        isActive = false;
        queue.remove(0);
        if (queue.size() > 0)
        {
            queue.get(0).start();
        }
    }

    private void dropChests()
    {

        Map<String, Location> locations = plugin.getData().getLocations(Data.VOTE_PARTY);

        Random random = new Random();
        Map<Integer, Boolean> tasks = new HashMap<>();

        for (String key : locations.keySet())
        {
            List<ItemStack> chest =
                    new ArrayList<>(Arrays.asList(plugin.getData().getItems(Data.VOTE_PARTY + "." + key)));

            Location loc = locations.get(key).clone().add(0.5, 0, 0.5);
            Location dropLoc = new Location(loc.getWorld(), loc.getX(), loc.getY() - 1, loc.getZ());
            Location fireworkLoc = new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ());

            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    if (!tasks.containsKey(getTaskId()))
                    {
                        tasks.put(getTaskId(), false);
                    }
                    if (votePartyType == VotePartyType.ALL_CHESTS_AT_ONCE.getValue() || !tasks.containsValue(true) || tasks.get(getTaskId()))
                    {
                        if (votePartyType == VotePartyType.ONE_CHEST_AT_A_TIME.getValue())
                        {
                            tasks.put(getTaskId(), true);
                        }
                        if (chest.size() > 0)
                        {
                            int i = random.nextInt(chest.size());
                            if (dropLoc.getWorld() != null)
                            {
                                dropLoc.getWorld().dropItem(dropLoc, chest.remove(i));
                            }
                            if (random.nextInt(2) == 0)
                            {
                                CustomVote.shootFirework(plugin, fireworkLoc);
                            }
                        } else
                        {
                            tasks.remove(getTaskId());
                            if (tasks.isEmpty())
                            {
                                plugin.getServer().broadcastMessage(Messages.VOTE_PARTY_END.getMessage(plugin));
                                stop();
                            }
                            cancel();
                        }
                    }
                }
            }.runTaskTimer(plugin, 0, 10);
        }
    }

    private void dropChestsRandomly()
    {
        Map<String, Location> locations = plugin.getData().getLocations(Data.VOTE_PARTY);
        Map<String, List<ItemStack>> chests = new HashMap<>();
        List<String> keys = new ArrayList<>(locations.keySet());
        keys.forEach(key ->
        {
            ItemStack[] items = plugin.getData().getItems(
                    Data.VOTE_PARTY + "." + key);
            if (items.length > 0)
            {
                chests.put(key,
                        new ArrayList<>(Arrays.asList(items)));
            }
        });

        Random random = new Random();
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (!keys.isEmpty() && !chests.isEmpty())
                {
                    String key = keys.get(random.nextInt(keys.size()));
                    List<ItemStack> chest = chests.get(key);
                    Location loc = locations.get(key).clone().add(0.5, 0, 0.5);
                    Location dropLoc = new Location(loc.getWorld(), loc.getX(), loc.getY() - 1, loc.getZ());
                    Location fireworkLoc = new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ());

                    if (dropLoc.getWorld() != null)
                    {
                        dropLoc.getWorld().dropItem(dropLoc, chest.remove(random.nextInt(chest.size())));
                        if (chest.isEmpty())
                        {
                            keys.remove(key);
                        } else
                        {
                            chests.put(key, chest);
                        }
                    }
                    if (random.nextInt(3) == 0)
                    {
                        CustomVote.shootFirework(plugin, fireworkLoc);
                    }
                } else
                {
                    plugin.getServer().broadcastMessage(Messages.VOTE_PARTY_END.getMessage(plugin));
                    stop();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 10);
    }

    private void addToInventory()
    {
        Random random = new Random();
        Map<Integer, Boolean> tasks = new HashMap<>();

        for (String key : plugin.getData().getLocations(Data.VOTE_PARTY).keySet())
        {
            List<ItemStack> chest =
                    new ArrayList<>(Arrays.asList(plugin.getData().getItems(Data.VOTE_PARTY + "." + key)));

            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    if (!tasks.containsKey(getTaskId()))
                    {
                        tasks.put(getTaskId(), false);
                    }
                    if (!tasks.containsValue(true) || tasks.get(getTaskId()))
                    {
                        tasks.put(getTaskId(), true);
                        if (chest.size() > 0)
                        {
                            List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
                            Player player = players.get(random.nextInt(players.size()));
                            ItemStack item = chest.remove(random.nextInt(chest.size()));
                            for(ItemStack left : player.getInventory().addItem(item).values())
                            {
                                player.getWorld().dropItem(player.getLocation(), left);
                            }
                            SoundType.PICKUP.play(plugin, player);
                        } else
                        {
                            tasks.remove(getTaskId());
                            if (tasks.isEmpty())
                            {
                                plugin.getServer().broadcastMessage(Messages.VOTE_PARTY_END.getMessage(plugin));
                                stop();
                            }
                            cancel();
                        }
                    }
                }
            }.runTaskTimer(plugin, 0, 10);
        }
    }
}
