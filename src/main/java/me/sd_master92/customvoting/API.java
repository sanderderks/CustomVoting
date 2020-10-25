package me.sd_master92.customvoting;

import com.vexsoftware.votifier.model.Vote;
import me.sd_master92.customfile.PlayerFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class API
{
    public static void forwardVote(String uuid, Vote vote, Main plugin)
    {
        new VoteFile(uuid, plugin).addVote();
        broadcastVote(vote, plugin);
        updateSigns(plugin);
    }

    public static void broadcastVote(Vote vote, Main plugin)
    {
        HashMap<String, String> placeholders = new HashMap<>();
        placeholders.put("%PLAYER%", vote.getUsername());
        placeholders.put("%SERVICE%", vote.getServiceName());
        String message = getMessage("broadcast", placeholders, plugin);
        plugin.getServer().broadcastMessage(message);
    }

    public static void updateSigns(Main plugin)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Map<String, Location> locations = plugin.getData().getLocations("vote_top");
                for (String key : locations.keySet())
                {
                    Location loc = locations.get(key);
                    int top = Integer.parseInt(key);
                    updateSign(loc, top, plugin);
                }
            }
        }.runTaskLater(plugin, 40L);
    }

    public static void updateSign(Location loc, int top, Main plugin)
    {
        VoteFile topVoter = getTopVoter(top - 1, plugin);
        if (topVoter != null && loc.getBlock().getState() instanceof Sign)
        {
            plugin.getData().setLocation("vote_top." + "" + top, loc);
            Sign sign = (Sign) loc.getBlock().getState();
            sign.setLine(0, ChatColor.translateAlternateColorCodes('&', "&4&lTop " + top + ":"));
            sign.setLine(1, ChatColor.translateAlternateColorCodes('&', "&b" + topVoter.getName()));
            sign.setLine(2, ChatColor.translateAlternateColorCodes('&', "&d" + topVoter.getVotes() + " votes"));
            sign.update(true);
            updateSkulls(loc, topVoter.getUuid(), plugin);
        }
    }

    public static void updateSkulls(Location loc, String uuid, Main plugin)
    {
        BlockData blockData = loc.getBlock().getBlockData();
        if (blockData instanceof WallSign)
        {
            WallSign sign = (WallSign) blockData;
            Block block1 = loc.add(0, 1, 0).getBlock();
            Block block2 = loc.add(0, 1, 0).getBlock().getRelative(sign.getFacing().getOppositeFace());
            for (Block block : new Block[]{block1, block2})
            {
                if (block.getState() instanceof Skull)
                {
                    Skull skull = (Skull) block.getState();
                    try
                    {
                        skull.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(uuid)));
                        skull.update(true);
                    } catch (Exception e)
                    {
                        if(skull.hasOwner())
                        {
                            Material material = block.getType();
                            block.setType(Material.AIR);
                            new BukkitRunnable()
                            {
                                @Override
                                public void run()
                                {
                                    block.setType(material);
                                    Skull skull = (Skull) block.getState();
                                    skull.setRotation(sign.getFacing());
                                    skull.update(true);
                                }
                            }.runTaskLater(plugin, 1L);
                        }
                    }
                }
            }
        }
    }

    public static List<VoteFile> getTopVoters(Main plugin)
    {
        List<VoteFile> voteFiles = new ArrayList<>();
        for (PlayerFile playerFile : PlayerFile.getAll(plugin))
        {
            voteFiles.add(new VoteFile(playerFile.getUuid(), plugin));
        }
        voteFiles.sort((x, y) ->
        {
            int compare = Integer.compare(y.getVotes(), x.getVotes());
            if(compare == 0)
            {
                compare = Long.compare(x.getTimeStamp("last"), y.getTimeStamp("last"));
            }
            return compare;
        });
        List<VoteFile> topVoters = new ArrayList<>();
        for (int i = 0; i < plugin.getSettings().getNumber("votetop_command") && i < voteFiles.size(); i++)
        {
            topVoters.add(voteFiles.get(i));
        }
        return topVoters;
    }

    public static VoteFile getTopVoter(int n, Main plugin)
    {
        List<VoteFile> topVoters = getTopVoters(plugin);
        if (n >= 0 && n < topVoters.size())
        {
            return topVoters.get(n);
        }
        return null;
    }

    public static String getMessage(String path, Map<String, String> placeholders, Main plugin)
    {
        String message = plugin.getMessages().getConfig().getString(path.toLowerCase());
        if (message != null)
        {
            message = ChatColor.translateAlternateColorCodes('&', message);
            if (placeholders != null)
            {
                for (String placeholder : placeholders.keySet())
                {
                    message = message.replace(placeholder, placeholders.get(placeholder));
                }
            }
            return message;
        }
        return "";
    }

    public static List<String> getMessages(String path, Map<String, String> placeholders, boolean replaceFirst, Main plugin)
    {
        List<String> messages = plugin.getMessages().getConfig().getStringList(path.toLowerCase());
        for (int i = 0; i < messages.size(); i++)
        {
            String message = ChatColor.translateAlternateColorCodes('&', messages.get(i));
            if (placeholders != null)
            {
                for (String placeholder : placeholders.keySet())
                {
                    if (replaceFirst)
                    {
                        message = message.replaceFirst(placeholder, placeholders.get(placeholder));
                    } else
                    {
                        message = message.replace(placeholder, placeholders.get(placeholder));
                    }
                }
            }
            messages.set(i, message);
        }
        return messages;
    }

    public static List<String> getMessages(String path, Map<String, String> placeholders, Main plugin)
    {
        return getMessages(path.toLowerCase(), placeholders, false, plugin);
    }
}
