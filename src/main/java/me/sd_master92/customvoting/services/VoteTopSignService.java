package me.sd_master92.customvoting.services;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.VoteFile;
import me.sd_master92.customvoting.constants.Data;
import me.sd_master92.customvoting.constants.Messages;
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

import java.util.*;

public class VoteTopSignService
{
    private final Main plugin;

    public VoteTopSignService(Main plugin)
    {
        this.plugin = plugin;
    }

    public void updateSigns()
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Map<String, Location> locations = plugin.getData().getLocations(Data.VOTE_TOP_SIGNS);
                for (String key : locations.keySet())
                {
                    Location loc = locations.get(key);
                    if (key.equals("title"))
                    {
                        updateSign(loc);
                    } else
                    {
                        int top = Integer.parseInt(key);
                        updateSign(loc, top);
                    }
                }
            }
        }.runTaskLater(plugin, 40L);
    }

    public void updateSign(Player player, Location loc)
    {
        if (loc.getBlock().getState() instanceof Sign)
        {
            plugin.getData().setLocation(Data.VOTE_TOP_SIGNS + ".title", loc);
            if(player != null)
            {
                player.sendMessage(ChatColor.GREEN + "Registered Vote Sign #title");
            }
            Sign sign = (Sign) loc.getBlock().getState();
            List<String> messages = plugin.getMessages().getMessages(Messages.VOTE_TOP_SIGNS_TITLE_SIGN);
            for (int i = 0; i < messages.size(); i++)
            {
                sign.setLine(i, messages.get(i));
            }
            sign.update(true);
        }
    }

    public void updateSign(Location loc)
    {
        updateSign(null, loc);
    }

    public void updateSign(Player player, Location loc, int top)
    {
        VoteFile topVoter = VoteFile.getTopVoter(plugin, top);
        if (loc.getBlock().getState() instanceof Sign)
        {
            Sign sign = (Sign) loc.getBlock().getState();
            if (topVoter != null)
            {
                Location oldLoc = plugin.getData().getLocation(Data.VOTE_TOP_SIGNS + "." + top);
                if (oldLoc == null)
                {
                    plugin.getData().setLocation(Data.VOTE_TOP_SIGNS + "." + top, loc);
                    if(player != null)
                    {
                        player.sendMessage(ChatColor.GREEN + "Registered Vote Sign #" + top);
                    }
                } else
                {
                    if (!oldLoc.equals(loc))
                    {
                        plugin.getData().setLocation(Data.VOTE_TOP_SIGNS + "." + top, loc);
                        if (oldLoc.getBlock().getState() instanceof Sign)
                        {
                            Sign oldSign = (Sign) oldLoc.getBlock().getState();
                            for (int i = 0; i < 4; i++)
                            {
                                if (i == 1)
                                {
                                    oldSign.setLine(i,
                                            plugin.getMessages().getMessage(Messages.VOTE_TOP_SIGNS_PLAYER_SIGNS_OUTDATED));
                                } else
                                {
                                    oldSign.setLine(i, "");
                                }
                            }
                            oldSign.update(true);
                        }
                    }
                }
                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("%NUMBER%", "" + top);
                placeholders.put("%PLAYER%", topVoter.getName());
                placeholders.put("%VOTES%", "" + topVoter.getVotes());
                placeholders.put("%s%", topVoter.getVotes() == 1 ? "" : "");
                List<String> messages = plugin.getMessages().getMessages(Messages.VOTE_TOP_SIGNS_PLAYER_SIGNS_FORMAT,
                        placeholders);
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
                                plugin.getMessages().getMessage(Messages.VOTE_TOP_SIGNS_PLAYER_SIGNS_NOT_FOUND));
                    } else
                    {
                        sign.setLine(i, "");
                    }
                }
            }
            sign.update(true);
        }
    }

    public void updateSign(Location loc, int top)
    {
        updateSign(null, loc, top);
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
}
