package me.sd_master92.customvoting.services;

import me.sd_master92.customfile.PlayerFile;
import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.VoteFile;
import me.sd_master92.customvoting.constants.types.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class VoteTopService
{
    private final Main plugin;

    public VoteTopService(Main plugin)
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
                Map<String, Location> locations = plugin.getData().getLocations("vote_top");
                for (String key : locations.keySet())
                {
                    Location loc = locations.get(key);
                    int top = Integer.parseInt(key);
                    updateSign(loc, top);
                }
            }
        }.runTaskLater(plugin, 40L);
    }

    public void updateSign(Location loc) {
        if (loc.getBlock().getState() instanceof Sign)
        {
            Sign sign = (Sign) loc.getBlock().getState();
            List<String> messages = plugin.getMessages().getMessages(Messages.VOTE_TOP_SIGNS_TITLE_SIGN);
            for (int i = 0; i < messages.size(); i++)
            {
                sign.setLine(i, messages.get(i));
            }
            sign.update(true);
        }
    }

    public void updateSign(Location loc, int top)
    {
        VoteFile topVoter = getTopVoter(top - 1);
        if (loc.getBlock().getState() instanceof Sign)
        {
            Sign sign = (Sign) loc.getBlock().getState();
            if (topVoter != null)
            {
                Location oldLoc = plugin.getData().getLocation("vote_top." + top);
                if (oldLoc != null)
                {
                    if (oldLoc.getBlock().getState() instanceof Sign)
                    {
                        Sign oldSign = (Sign) oldLoc.getBlock().getState();
                        oldSign.setLine(0, plugin.getMessages().getMessage(Messages.VOTE_TOP_SIGNS_PLAYER_SIGNS_OUTDATED));
                        oldSign.update(true);
                    }
                }
                plugin.getData().setLocation("vote_top." + "" + top, loc);
                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("%NUMBER%", "" + top);
                placeholders.put("%PLAYER%", topVoter.getName());
                placeholders.put("%VOTES%", "" + topVoter.getVotes());
                List<String> messages = plugin.getMessages().getMessages(Messages.VOTE_TOP_SIGNS_PLAYER_SIGNS_FORMAT, placeholders);
                for (int i = 0; i < messages.size(); i++)
                {
                    sign.setLine(i, messages.get(i));
                }
                updateSkulls(loc, topVoter.getUuid());
            } else
            {
                sign.setLine(0, plugin.getMessages().getMessage(Messages.VOTE_TOP_SIGNS_PLAYER_SIGNS_NOT_FOUND));
            }
            sign.update(true);
        }
    }

    public void updateSkulls(Location loc, String uuid)
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
                                    if(material == Material.PLAYER_WALL_HEAD)
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

    public List<VoteFile> getTopVoters()
    {
        List<VoteFile> topVoters = new ArrayList<>();
        for (PlayerFile playerFile : PlayerFile.getAll(plugin))
        {
            topVoters.add(new VoteFile(playerFile.getUuid(), plugin));
        }
        topVoters.sort((x, y) ->
        {
            int compare = Integer.compare(y.getVotes(), x.getVotes());
            if (compare == 0)
            {
                compare = Long.compare(x.getTimeStamp("last"), y.getTimeStamp("last"));
            }
            return compare;
        });
        return topVoters;
    }

    public VoteFile getTopVoter(int n)
    {
        List<VoteFile> topVoters = getTopVoters();
        if (n >= 0 && n < topVoters.size())
        {
            return topVoters.get(n);
        }
        return null;
    }
}
