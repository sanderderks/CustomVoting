package me.sd_master92.customvoting.commands;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.subjects.VoteTopStand;
import me.sd_master92.plugin.command.SimpleCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeleteTopCommand extends SimpleCommand
{
    public DeleteTopCommand(Main plugin)
    {
        super(plugin, "deletetop", false);
        withPlayer();
        withUsage(ChatColor.RED + "- /deletetop <top>");
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
            VoteTopStand voteTopStand = VoteTopStand.get(top);
            if (voteTopStand != null)
            {
                voteTopStand.delete(player);
            } else
            {
                player.sendMessage(ChatColor.RED + "That Vote Stand does not exist.");
            }
        } catch (Exception e)
        {
            player.sendMessage(ChatColor.RED + "Invalid argument: 'top' must be a number.");
        }
    }
}
