package me.sd_master92.customvoting.commands;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.subjects.VoteTopStand;
import me.sd_master92.plugin.command.SimpleCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateTopCommand extends SimpleCommand
{
    private final Main plugin;

    public CreateTopCommand(Main plugin)
    {
        super(plugin, "createtop", false);
        withPlayer();
        withUsage(ChatColor.RED + "- /createtop <top>");
        this.plugin = plugin;
    }

    @Override
    public void onCommand(CommandSender commandSender, String[] args)
    {

    }

    @Override
    public void onCommand(Player player, String[] args)
    {
        try
        {
            int top = Integer.parseInt(args[0]);
            if (top > 0)
            {
                new VoteTopStand(plugin, top, player);
            } else
            {
                player.sendMessage(ChatColor.RED + "Invalid argument: 'top' must be a positive number.");
            }
        } catch (Exception e)
        {
            player.sendMessage(ChatColor.RED + "Invalid argument: 'top' must be a number.");
        }
    }
}

