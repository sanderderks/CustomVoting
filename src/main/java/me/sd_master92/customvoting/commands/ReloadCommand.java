package me.sd_master92.customvoting.commands;

import me.sd_master92.customfile.CustomFile;
import me.sd_master92.customfile.PlayerFile;
import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.Messages;
import me.sd_master92.customvoting.subjects.VoteTopSign;
import me.sd_master92.customvoting.subjects.VoteTopStand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor
{
    private final Main plugin;

    public ReloadCommand(Main plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(command.getPermission() != null && sender.hasPermission(command.getPermission()))
        {
            if (plugin.getSettings().reloadConfig() && plugin.getData().reloadConfig() && plugin.getMessages().reloadConfig())
            {
                if (PlayerFile.getAll(plugin).stream().allMatch(CustomFile::reloadConfig))
                {
                    VoteTopSign.updateAll(plugin);
                    VoteTopStand.updateAll(plugin);
                    sender.sendMessage(ChatColor.GREEN + "Configuration files reloaded!");
                } else
                {
                    sender.sendMessage(ChatColor.RED + "Could not reload configuration files!");
                }
            }
        } else
        {
            sender.sendMessage(Messages.NO_PERMISSION.getMessage(plugin));
        }
        return true;
    }
}
