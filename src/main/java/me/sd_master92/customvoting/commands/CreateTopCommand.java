package me.sd_master92.customvoting.commands;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.Data;
import me.sd_master92.customvoting.constants.Messages;
import me.sd_master92.customvoting.services.GUIService;
import me.sd_master92.customvoting.services.VoteTopStandService;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;

public class CreateTopCommand implements CommandExecutor
{
    private final Main plugin;
    private final VoteTopStandService voteTopStandService;

    public CreateTopCommand(Main plugin)
    {
        this.plugin = plugin;
        voteTopStandService = new VoteTopStandService(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            if (command.getPermission() != null && sender.hasPermission(command.getPermission()))
            {
                if (args.length == 0)
                {
                    sender.sendMessage(ChatColor.RED + "- /createtop <top>");
                } else
                {
                    int top = 0;
                    try
                    {
                        top = Integer.parseInt(args[0]);
                    } catch (Exception e)
                    {
                        sender.sendMessage(ChatColor.RED + "Invalid argument: 'top' must be a number.");
                    }
                    try
                    {
                        if(plugin.getData().get(Data.VOTE_TOP_STANDS + "." + top) == null)
                        {
                            World world = player.getLocation().getWorld();
                            if (world != null)
                            {
                                ArmorStand topStand =
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

                                topStand.setCustomName(ChatColor.GREEN + "" + ChatColor.BOLD + "Top " + top + ":");

                                ArmorStand nameStand =
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

                                ArmorStand armorStand = (ArmorStand) world.spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
                                plugin.getData().set(Data.VOTE_TOP_STANDS + "." + top + ".votes",
                                        armorStand.getUniqueId().toString());
                                plugin.getData().saveConfig();

                                armorStand.setRemoveWhenFarAway(false);
                                armorStand.setSilent(true);
                                armorStand.setPersistent(true);
                                armorStand.setGravity(false);
                                armorStand.setCustomNameVisible(true);

                                armorStand.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);
                                armorStand.addEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.REMOVING_OR_CHANGING);
                                armorStand.addEquipmentLock(EquipmentSlot.LEGS, ArmorStand.LockType.REMOVING_OR_CHANGING);
                                armorStand.addEquipmentLock(EquipmentSlot.FEET, ArmorStand.LockType.REMOVING_OR_CHANGING);
                                armorStand.addEquipmentLock(EquipmentSlot.HAND, ArmorStand.LockType.REMOVING_OR_CHANGING);
                                EntityEquipment entityEquipment = armorStand.getEquipment();
                                if (entityEquipment != null)
                                {
                                    switch (top)
                                    {
                                        case 1:
                                            entityEquipment.setChestplate(GUIService.createItem(Material.DIAMOND_CHESTPLATE, true));
                                            entityEquipment.setLeggings(GUIService.createItem(Material.DIAMOND_LEGGINGS, true));
                                            entityEquipment.setBoots(GUIService.createItem(Material.DIAMOND_BOOTS, true));
                                            entityEquipment.setItemInMainHand(GUIService.createItem(Material.DIAMOND_SWORD,
                                                    true));
                                            break;
                                        case 2:
                                            entityEquipment.setChestplate(GUIService.createItem(Material.GOLDEN_CHESTPLATE, true));
                                            entityEquipment.setLeggings(GUIService.createItem(Material.GOLDEN_LEGGINGS, true));
                                            entityEquipment.setBoots(GUIService.createItem(Material.GOLDEN_BOOTS, true));
                                            entityEquipment.setItemInMainHand(GUIService.createItem(Material.GOLDEN_SWORD,
                                                    true));
                                            break;
                                        case 3:
                                            entityEquipment.setChestplate(GUIService.createItem(Material.IRON_CHESTPLATE, true));
                                            entityEquipment.setLeggings(GUIService.createItem(Material.IRON_LEGGINGS, true));
                                            entityEquipment.setBoots(GUIService.createItem(Material.IRON_BOOTS, true));
                                            entityEquipment.setItemInMainHand(GUIService.createItem(Material.IRON_SWORD,
                                                    true));
                                            break;
                                        default:
                                            entityEquipment.setChestplate(GUIService.createItem(Material.CHAINMAIL_CHESTPLATE, true));
                                            entityEquipment.setLeggings(GUIService.createItem(Material.CHAINMAIL_LEGGINGS, true));
                                            entityEquipment.setBoots(GUIService.createItem(Material.CHAINMAIL_BOOTS, true));
                                            entityEquipment.setItemInMainHand(GUIService.createItem(Material.STONE_SWORD, true));
                                    }
                                }
                            }
                            voteTopStandService.updateStands();
                            player.sendMessage(ChatColor.GREEN + "Successfully created Vote Stand #" + top);
                        } else
                        {
                            player.sendMessage(ChatColor.RED + "That Vote Stand already exists.");
                        }
                    } catch (Exception e)
                    {
                        player.sendMessage(plugin.getMessages().getMessage(Messages.EXCEPTION));
                    }
                }
            } else
            {
                player.sendMessage(plugin.getMessages().getMessage(Messages.NO_PERMISSION));
            }
        } else
        {
            sender.sendMessage(plugin.getMessages().getMessage(Messages.NO_PERMISSION));
        }
        return true;
    }
}

