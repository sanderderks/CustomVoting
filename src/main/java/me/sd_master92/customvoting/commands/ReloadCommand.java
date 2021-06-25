package me.sd_master92.customvoting.commands;

import me.sd_master92.customfile.CustomFile;
import me.sd_master92.customfile.PlayerFile;
import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.subjects.VoteTopSign;
import me.sd_master92.customvoting.subjects.VoteTopStand;
import me.sd_master92.plugin.command.SimpleCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand extends SimpleCommand
{
    private final Main plugin;

    public ReloadCommand(Main plugin)
    {
        super(plugin, "votereload");
        this.plugin = plugin;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args)
    {
        if (plugin.getConfig().reloadConfig() && plugin.getData().reloadConfig() && plugin.getMessages().reloadConfig())
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
    }

    @Override
    public void onCommand(Player player, String[] strings)
    {

    }
}
