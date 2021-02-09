package me.sd_master92.customvoting.commands;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.Data;
import me.sd_master92.customvoting.constants.Messages;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DeleteTopCommand implements CommandExecutor
{
    private final Main plugin;

    public DeleteTopCommand(Main plugin)
    {
        this.plugin = plugin;
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
                    sender.sendMessage(ChatColor.RED + "- /deletetop <top>");
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
                        if (plugin.getData().get(Data.VOTE_TOP_STANDS + "." + top) == null)
                        {
                            player.sendMessage(ChatColor.RED + "That Vote Stand does not exist.");
                            return true;
                        }
                        World world = player.getLocation().getWorld();
                        if (world != null)
                        {
                            String topUuid = plugin.getData().getString(Data.VOTE_TOP_STANDS + "." + top + ".top");
                            if (topUuid != null)
                            {
                                Entity topEntity = plugin.getServer().getEntity(UUID.fromString(topUuid));
                                if (topEntity != null)
                                {
                                    topEntity.remove();
                                }
                            }
                            String nameUuid = plugin.getData().getString(Data.VOTE_TOP_STANDS + "." + top + ".name");
                            if (nameUuid != null)
                            {
                                Entity nameEntity = plugin.getServer().getEntity(UUID.fromString(nameUuid));
                                if (nameEntity != null)
                                {
                                    nameEntity.remove();
                                }
                            }
                            String votesUuid = plugin.getData().getString(Data.VOTE_TOP_STANDS + "." + top +
                                    ".votes");
                            if (votesUuid != null)
                            {
                                Entity votesEntity = plugin.getServer().getEntity(UUID.fromString(votesUuid));
                                if (votesEntity != null)
                                {
                                    votesEntity.remove();
                                }
                            }
                            plugin.getData().set(Data.VOTE_TOP_STANDS + "." + top, null);
                            plugin.getData().saveConfig();
                            player.sendMessage(ChatColor.GREEN + "Successfully deleted Vote Stand #" + top);
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
