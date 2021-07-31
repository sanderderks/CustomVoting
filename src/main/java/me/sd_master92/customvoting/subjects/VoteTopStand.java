package me.sd_master92.customvoting.subjects;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.VoteFile;
import me.sd_master92.customvoting.constants.Data;
import me.sd_master92.customvoting.constants.Messages;
import me.sd_master92.customvoting.constants.Voter;
import me.sd_master92.customvoting.database.PlayerTable;
import me.sd_master92.customvoting.gui.GUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VoteTopStand
{
    private static final Map<Integer, VoteTopStand> voteTops = new HashMap<>();

    private final Main plugin;
    private final int top;

    private ArmorStand topStand;
    private ArmorStand nameStand;
    private ArmorStand votesStand;

    public VoteTopStand(Main plugin, int top, Player player)
    {
        this.plugin = plugin;
        this.top = top;

        ConfigurationSection section = plugin.getData().getConfigurationSection(Data.VOTE_TOP_STANDS + "." + top);
        if (section != null)
        {
            if (player != null)
            {
                player.sendMessage(ChatColor.RED + "That Vote Stand already exists.");
                return;
            } else
            {
                topStand = getArmorStand(section.getString("top"));
                nameStand = getArmorStand(section.getString("name"));
                votesStand = getArmorStand(section.getString("votes"));
            }
        } else if (player != null)
        {
            create(player);
        }

        voteTops.put(top, this);
        update();
    }

    public VoteTopStand(Main plugin, int top)
    {
        this(plugin, top, null);
    }

    public static VoteTopStand get(int top)
    {
        return voteTops.get(top);
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
                for (VoteTopStand voteTop : voteTops.values())
                {
                    voteTop.update();
                }
            }
        }.runTaskLater(plugin, 40L);
    }

    private static void initialize(Main plugin)
    {
        ConfigurationSection section = plugin.getData().getConfigurationSection(Data.VOTE_TOP_STANDS);
        if (section != null)
        {
            for (String n : section.getKeys(false))
            {
                try
                {
                    int top = Integer.parseInt(n);
                    new VoteTopStand(plugin, top);
                } catch (Exception ignored)
                {

                }
            }
        }
    }

    private ArmorStand getArmorStand(String uuid)
    {
        if (uuid != null)
        {
            Entity entity =
                    plugin.getServer().getEntity(UUID.fromString(uuid));
            if (entity instanceof ArmorStand)
            {
                return (ArmorStand) entity;
            }
        }
        return null;
    }

    private void create(Player player)
    {
        World world = player.getLocation().getWorld();
        if (world != null)
        {
            topStand =
                    (ArmorStand) world.spawnEntity(player.getLocation().add(0, 1, 0),
                            EntityType.ARMOR_STAND);
            plugin.getData().set(Data.VOTE_TOP_STANDS + "." + top + ".top",
                    topStand.getUniqueId().toString());

            topStand.setVisible(false);
            topStand.setRemoveWhenFarAway(false);
            topStand.setSilent(true);
            topStand.setPersistent(true);
            topStand.setGravity(false);
            topStand.setCustomNameVisible(true);

            nameStand =
                    (ArmorStand) world.spawnEntity(player.getLocation().add(0, 0.5, 0),
                            EntityType.ARMOR_STAND);
            plugin.getData().set(Data.VOTE_TOP_STANDS + "." + top + ".name",
                    nameStand.getUniqueId().toString());

            nameStand.setVisible(false);
            nameStand.setRemoveWhenFarAway(false);
            nameStand.setSilent(true);
            nameStand.setPersistent(true);
            nameStand.setGravity(false);
            nameStand.setCustomNameVisible(true);

            votesStand = (ArmorStand) world.spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
            plugin.getData().set(Data.VOTE_TOP_STANDS + "." + top + ".votes",
                    votesStand.getUniqueId().toString());
            plugin.getData().saveConfig();

            votesStand.setRemoveWhenFarAway(false);
            votesStand.setSilent(true);
            votesStand.setPersistent(true);
            votesStand.setGravity(false);
            votesStand.setCustomNameVisible(true);

            votesStand.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);
            votesStand.addEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.REMOVING_OR_CHANGING);
            votesStand.addEquipmentLock(EquipmentSlot.LEGS, ArmorStand.LockType.REMOVING_OR_CHANGING);
            votesStand.addEquipmentLock(EquipmentSlot.FEET, ArmorStand.LockType.REMOVING_OR_CHANGING);
            votesStand.addEquipmentLock(EquipmentSlot.HAND, ArmorStand.LockType.REMOVING_OR_CHANGING);
            EntityEquipment entityEquipment = votesStand.getEquipment();
            if (entityEquipment != null)
            {
                switch (top)
                {
                    case 1:
                        entityEquipment.setChestplate(GUI.createItem(Material.DIAMOND_CHESTPLATE, true));
                        entityEquipment.setLeggings(GUI.createItem(Material.DIAMOND_LEGGINGS, true));
                        entityEquipment.setBoots(GUI.createItem(Material.DIAMOND_BOOTS, true));
                        entityEquipment.setItemInMainHand(GUI.createItem(Material.DIAMOND_SWORD,
                                true));
                        break;
                    case 2:
                        entityEquipment.setChestplate(GUI.createItem(Material.GOLDEN_CHESTPLATE, true));
                        entityEquipment.setLeggings(GUI.createItem(Material.GOLDEN_LEGGINGS, true));
                        entityEquipment.setBoots(GUI.createItem(Material.GOLDEN_BOOTS, true));
                        entityEquipment.setItemInMainHand(GUI.createItem(Material.GOLDEN_SWORD,
                                true));
                        break;
                    case 3:
                        entityEquipment.setChestplate(GUI.createItem(Material.IRON_CHESTPLATE, true));
                        entityEquipment.setLeggings(GUI.createItem(Material.IRON_LEGGINGS, true));
                        entityEquipment.setBoots(GUI.createItem(Material.IRON_BOOTS, true));
                        entityEquipment.setItemInMainHand(GUI.createItem(Material.IRON_SWORD,
                                true));
                        break;
                    default:
                        entityEquipment.setChestplate(GUI.createItem(Material.CHAINMAIL_CHESTPLATE, true));
                        entityEquipment.setLeggings(GUI.createItem(Material.CHAINMAIL_LEGGINGS, true));
                        entityEquipment.setBoots(GUI.createItem(Material.CHAINMAIL_BOOTS, true));
                        entityEquipment.setItemInMainHand(GUI.createItem(Material.STONE_SWORD, true));
                }
            }
        }
        player.sendMessage(ChatColor.GREEN + "Registered Vote Stand #" + top);
    }

    private void update()
    {
        Voter voteFile = plugin.useDatabase() ? PlayerTable.getTopVoter(plugin, top) : VoteFile.getTopVoter(plugin,
                top);

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

        topStand.setCustomName(Messages.VOTE_TOP_STANDS_TOP.getMessage(plugin, placeholders));
        nameStand.setCustomName(Messages.VOTE_TOP_STANDS_CENTER.getMessage(plugin, placeholders));
        votesStand.setCustomName(Messages.VOTE_TOP_STANDS_BOTTOM.getMessage(plugin, placeholders));

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

    public void delete(Player player)
    {
        topStand.remove();
        nameStand.remove();
        votesStand.remove();

        plugin.getData().set(Data.VOTE_TOP_STANDS + "." + top, null);
        plugin.getData().saveConfig();

        voteTops.remove(top);

        player.sendMessage(ChatColor.GREEN + "Successfully deleted Vote Stand #" + top);
    }
}