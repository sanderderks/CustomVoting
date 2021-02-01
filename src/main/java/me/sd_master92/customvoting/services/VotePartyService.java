package me.sd_master92.customvoting.services;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.Messages;
import me.sd_master92.customvoting.constants.Settings;
import me.sd_master92.customvoting.constants.Sounds;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class VotePartyService
{
    public static final ItemStack VOTE_PARTY_ITEM = GUIService.createItem(Material.ENDER_CHEST,
            ChatColor.LIGHT_PURPLE +
                    "Vote Party Chest",
            ChatColor.GRAY + "Place this chest somewhere in the sky.;" + ChatColor.GRAY + "The contents of this chest" +
                    " will;" +
                    ChatColor.GRAY + "start dropping when the voteparty starts.");

    private final Main plugin;

    public VotePartyService(Main plugin)
    {
        this.plugin = plugin;
    }

    private void dropItems()
    {
        Map<String, Location> chests = plugin.getData().getLocations("voteparty");
        Random random = new Random();
        Map<Integer, Boolean> tasks = new HashMap<>();
        for (String key : chests.keySet())
        {
            Location loc = chests.get(key).add(0.5, 0, 0.5);
            Location dropLoc = new Location(loc.getWorld(), loc.getX(), loc.getY() - 1, loc.getZ());
            Location fireworkLoc = new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ());
            List<ItemStack> items = new ArrayList<>(Arrays.asList(plugin.getData().getItems("voteparty." + key)));
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    if (!tasks.containsKey(getTaskId()))
                    {
                        tasks.put(getTaskId(), true);
                    }
                    if (items.size() > 0)
                    {
                        int i = random.nextInt(items.size());
                        if (dropLoc.getWorld() != null)
                        {
                            dropLoc.getWorld().dropItem(dropLoc, items.remove(i));
                        }
                        if (random.nextInt(3) == 0)
                        {
                            VoteService.shootFirework(plugin, fireworkLoc);
                        }
                    } else
                    {
                        tasks.remove(getTaskId());
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 0, 10);
        }
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (tasks.isEmpty())
                {
                    plugin.getServer().broadcastMessage(plugin.getMessages().getMessage(Messages.VOTE_PARTY_END));
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    private void playForAll(Sounds sound)
    {
        for (Player player : plugin.getServer().getOnlinePlayers())
        {
            sound.play(plugin, player.getLocation());
        }
    }

    public void start()
    {
        new BukkitRunnable()
        {
            int count = plugin.getSettings().getNumber(Settings.VOTE_PARTY_COUNTDOWN);

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
                        playForAll(Sounds.NOTIFY);
                        plugin.getServer().broadcastMessage(plugin.getMessages().getMessage(Messages.VOTE_PARTY_COUNTDOWN, placeholders));
                        break;
                    case 5:
                    case 4:
                    case 3:
                    case 2:
                    case 1:
                        placeholders = new HashMap<>();
                        placeholders.put("%TIME%", "" + count);
                        playForAll(Sounds.NOTIFY);
                        plugin.getServer().broadcastMessage(plugin.getMessages().getMessage(Messages.VOTE_PARTY_COUNTDOWN_ENDING, placeholders));
                        break;
                    case 0:
                        playForAll(Sounds.VOTE_PARTY_START);
                        plugin.getServer().broadcastMessage(plugin.getMessages().getMessage(Messages.VOTE_PARTY_START));
                        dropItems();
                        cancel();
                }
                count--;
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    public void saveRewards(Player player, String key, ItemStack[] contents)
    {
        if (plugin.getData().setItems("voteparty." + key, contents))
        {
            Sounds.SUCCESS.play(plugin, player.getLocation());
            player.sendMessage(ChatColor.GREEN + "Successfully updated Vote Party Chest #" + key);
        } else
        {
            Sounds.FAILURE.play(plugin, player.getLocation());
            player.sendMessage(ChatColor.RED + "Failed to update Vote Party Chest #" + key);
        }
    }
}
