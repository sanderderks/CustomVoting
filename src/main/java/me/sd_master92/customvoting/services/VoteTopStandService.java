package me.sd_master92.customvoting.services;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.VoteFile;
import me.sd_master92.customvoting.constants.Data;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class VoteTopStandService
{
    private final Main plugin;

    public VoteTopStandService(Main plugin)
    {
        this.plugin = plugin;
    }

    public void updateStands()
    {
        ConfigurationSection section = plugin.getData().getConfigurationSection(Data.VOTE_TOP_STANDS);
        if (section != null)
        {
            for (String n : section.getKeys(false))
            {
                try
                {
                    int top = Integer.parseInt(n);
                    VoteFile voteFile = VoteFile.getTopVoter(plugin, top);

                    String nameUuid = section.getString(top + ".name");
                    if (nameUuid != null)
                    {
                        Entity nameEntity =
                                plugin.getServer().getEntity(UUID.fromString(nameUuid));
                        if (nameEntity instanceof ArmorStand)
                        {
                            ArmorStand nameStand = (ArmorStand) nameEntity;
                            if (voteFile != null)
                            {
                                nameStand.setCustomName(ChatColor.AQUA + voteFile.getName());
                            } else
                            {
                                nameStand.setCustomName(ChatColor.RED + "Unknown");
                            }
                        }
                    }
                    String votesUuid = section.getString(top + ".votes");
                    if (votesUuid != null)
                    {
                        Entity votesEntity =
                                plugin.getServer().getEntity(UUID.fromString(votesUuid));
                        if (votesEntity instanceof ArmorStand)
                        {
                            ArmorStand votesStand = (ArmorStand) votesEntity;
                            if (voteFile != null)
                            {
                                votesStand.setCustomName(ChatColor.GRAY + "Votes: " + ChatColor.LIGHT_PURPLE + voteFile.getVotes());
                            } else
                            {
                                votesStand.setCustomName(ChatColor.GRAY + "Votes: " + ChatColor.LIGHT_PURPLE + 0);
                            }
                            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
                            SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
                            if (skullMeta != null && voteFile != null)
                            {
                                try
                                {
                                    skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(voteFile.getUuid())));
                                    skull.setItemMeta(skullMeta);
                                } catch (Exception ignored)
                                {
                                }
                            }
                            EntityEquipment entityEquipment = votesStand.getEquipment();
                            if (entityEquipment != null)
                            {
                                entityEquipment.setHelmet(skull);
                            }
                        }
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
