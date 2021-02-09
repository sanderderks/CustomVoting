package me.sd_master92.customvoting.services;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.VoteFile;
import me.sd_master92.customvoting.constants.Data;
import me.sd_master92.customvoting.constants.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;
import java.util.Map;
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

                    Map<String, String> placeholders = new HashMap<>();
                    placeholders.put("%TOP%", "" + top);
                    if (voteFile != null)
                    {
                        placeholders.put("%PLAYER%", voteFile.getName());
                        placeholders.put("%VOTES%", "" + voteFile.getVotes());
                    } else
                    {
                        placeholders.put("%PLAYER%", ChatColor.RED + "Unknown");
                        placeholders.put("%VOTES%", "" + 0);
                    }

                    String topUuid = section.getString(top + ".top");
                    if (topUuid != null)
                    {
                        Entity topEntity =
                                plugin.getServer().getEntity(UUID.fromString(topUuid));
                        if (topEntity instanceof ArmorStand)
                        {
                            ArmorStand topStand = (ArmorStand) topEntity;
                            topStand.setCustomName(plugin.getMessages().getMessage(Messages.VOTE_TOP_STANDS_TOP,
                                    placeholders));
                        }
                    }

                    String nameUuid = section.getString(top + ".name");
                    if (nameUuid != null)
                    {
                        Entity nameEntity =
                                plugin.getServer().getEntity(UUID.fromString(nameUuid));
                        if (nameEntity instanceof ArmorStand)
                        {
                            ArmorStand nameStand = (ArmorStand) nameEntity;
                            nameStand.setCustomName(plugin.getMessages().getMessage(Messages.VOTE_TOP_STANDS_CENTER,
                                    placeholders));
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
                            votesStand.setCustomName(plugin.getMessages().getMessage(Messages.VOTE_TOP_STANDS_BOTTOM,
                                    placeholders));

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
