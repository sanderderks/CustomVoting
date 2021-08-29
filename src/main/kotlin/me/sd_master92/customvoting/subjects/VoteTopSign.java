package me.sd_master92.customvoting.subjects;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.VoteFile;
import me.sd_master92.customvoting.constants.Data;
import me.sd_master92.customvoting.constants.Messages;
import me.sd_master92.customvoting.constants.Voter;
import me.sd_master92.customvoting.database.PlayerTable;
import me.sd_master92.customvoting.extensions.CustomPlaceholders;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class VoteTopSign
{
    private static final Map<Integer, VoteTopSign> voteTops = new HashMap<>();

    private final Main plugin;
    private final int top;
    private final Location loc;

    public VoteTopSign(Main plugin, int top, Location loc, Player player)
    {
        this.plugin = plugin;
        this.top = top;
        this.loc = loc;

        if (player != null && (top == 0 || !voteTops.containsKey(top)))
        {
            player.sendMessage(ChatColor.GREEN + "Registered Vote Sign #" + (top == 0 ? "title" : top));
        }

        voteTops.put(top, this);
        update();
    }

    public VoteTopSign(Main plugin, int top, Location loc)
    {
        this(plugin, top, loc, null);
    }

    public static VoteTopSign get(Location loc)
    {
        for (int key : voteTops.keySet())
        {
            VoteTopSign voteTop = voteTops.get(key);
            if (voteTop.getLocation().equals(loc))
            {
                return voteTop;
            }
        }
        return null;
    }

    public static void updateAll(Main plugin)
    {
        if (voteTops.isEmpty())
        {
            initialize(plugin);
        }
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                for (VoteTopSign voteTop : voteTops.values())
                {
                    voteTop.update();
                }
            }
        }.runTaskLater(plugin, 40L);
    }

    private static void initialize(Main plugin)
    {
        Map<String, Location> locations = plugin.getData().getLocations(Data.VOTE_TOP_SIGNS);
        for (String key : locations.keySet())
        {
            Location loc = locations.get(key);
            try
            {
                new VoteTopSign(plugin, Integer.parseInt(key), loc);
            } catch (Exception ignored)
            {
                new VoteTopSign(plugin, 0, loc);
            }
        }
    }

    public Location getLocation()
    {
        return loc;
    }

    private void update()
    {
        if (top == 0)
        {
            updateTitle();
        } else
        {
            updateSign();
        }
    }

    private void updateTitle()
    {
        if (loc.getBlock().getState() instanceof Sign)
        {
            Sign sign = (Sign) loc.getBlock().getState();
            List<String> messages = Messages.VOTE_TOP_SIGNS_TITLE_SIGN.getMessages(plugin);
            for (int i = 0; i < messages.size(); i++)
            {
                sign.setLine(i, messages.get(i));
            }
            sign.update(true);
        }
    }

    private void updateSign()
    {
        if (loc.getBlock().getState() instanceof Sign)
        {
            Sign sign = (Sign) loc.getBlock().getState();
            Voter topVoter = plugin.hasDatabaseConnection() ? PlayerTable.getTopVoter(plugin, top) : VoteFile.getTopVoter(plugin,
                    top);

            if (topVoter != null)
            {
                Location oldLoc = plugin.getData().getLocation(Data.VOTE_TOP_SIGNS + "." + top);
                if (oldLoc != null && !oldLoc.equals(loc))
                {
                    if (oldLoc.getBlock().getState() instanceof Sign)
                    {
                        Sign oldSign = (Sign) oldLoc.getBlock().getState();
                        for (int i = 0; i < 4; i++)
                        {
                            if (i == 1)
                            {
                                oldSign.setLine(i,
                                        Messages.VOTE_TOP_SIGNS_PLAYER_SIGNS_OUTDATED.getMessage(plugin));
                            } else
                            {
                                oldSign.setLine(i, "");
                            }
                        }
                        oldSign.update(true);
                    }
                }

                plugin.getData().setLocation(Data.VOTE_TOP_SIGNS + "." + top, loc);

                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("%NUMBER%", "" + top);
                placeholders.put("%PLAYER%", topVoter.getName());
                placeholders.put("%VOTES%", "" + topVoter.getVotes());
                placeholders.put("%s%", topVoter.getVotes() == 1 ? "" : "s");
                List<String> messages = Messages.VOTE_TOP_SIGNS_PLAYER_SIGNS_FORMAT.getMessages(plugin, placeholders);
                for (int i = 0; i < messages.size(); i++)
                {
                    sign.setLine(i, messages.get(i));
                }
                updateSkull(loc, topVoter.getUuid());
            } else
            {
                for (int i = 0; i < 4; i++)
                {
                    if (i == 1)
                    {
                        sign.setLine(i,
                                Messages.VOTE_TOP_SIGNS_PLAYER_SIGNS_NOT_FOUND.getMessage(plugin));
                    } else
                    {
                        sign.setLine(i, "");
                    }
                }
            }
            sign.update(true);
        }
    }

    public void updateSkull(Location loc, String uuid)
    {
        BlockData blockData = loc.getBlock().getBlockData();
        if (blockData instanceof WallSign)
        {
            WallSign sign = (WallSign) blockData;
            Block block1 = loc.getBlock().getRelative(BlockFace.UP);
            Block block2 = loc.getBlock().getRelative(sign.getFacing().getOppositeFace()).getRelative(BlockFace.UP);
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
                        if (skull.hasOwner())
                        {
                            Material material = skull.getType();
                            BlockFace blockFace = skull.getRotation();

                            block.setType(Material.AIR);
                            new BukkitRunnable()
                            {
                                @Override
                                public void run()
                                {
                                    block.setType(material);
                                    Skull skull = (Skull) block.getState();
                                    if (material == Material.PLAYER_WALL_HEAD)
                                    {
                                        skull.setRotation(sign.getFacing());
                                    } else
                                    {
                                        skull.setRotation(blockFace);
                                    }
                                    skull.update(true);
                                }
                            }.runTaskLater(plugin, 1L);
                        }
                    }
                }
            }
        }
    }

    public void delete(Player player)
    {
        if (plugin.getData().deleteLocation(Data.VOTE_TOP_SIGNS + "." + (top == 0 ? "title" : top)))
        {
            voteTops.remove(top);

            player.sendMessage(ChatColor.RED + "Unregistered Vote Sign #" + (top == 0 ? "title" : top));
        }
    }
}
