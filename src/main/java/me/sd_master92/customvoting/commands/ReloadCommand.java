package me.sd_master92.customvoting.commands;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import me.sd_master92.customfile.PlayerFile;
import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Date;

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
                boolean success = true;
                for (PlayerFile playerFile : PlayerFile.getAll(plugin))
                {
                    if (!playerFile.reloadConfig())
                    {
                        success = false;
                    }
                }
                if (success)
                {
                    sender.sendMessage(ChatColor.GREEN + "Configuration files reloaded!");
                } else
                {
                    sender.sendMessage(ChatColor.RED + "Could not reload configuration files!");
                }
            }
        } else
        {
            sender.sendMessage(Messages.NO_PERMISSION);
        }
        return true;
    }
}
