package me.sd_master92.customvoting.commands;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.Data;
import me.sd_master92.customvoting.constants.Messages;
import me.sd_master92.customvoting.services.VotePartyService;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VotePartyCommand implements CommandExecutor
{
    private final Main plugin;
    private final VotePartyService votePartyService;

    public VotePartyCommand(Main plugin)
    {
        this.plugin = plugin;
        votePartyService = new VotePartyService(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (command.getPermission() != null && sender.hasPermission(command.getPermission()))
        {
            if(sender instanceof Player)
            {
                Player player = (Player) sender;
                if (args.length == 0)
                {
                    sender.sendMessage(ChatColor.RED + "- /voteparty create | start");
                } else if (args.length == 1)
                {
                    switch (args[0])
                    {
                        case "create":
                            player.getInventory().addItem(VotePartyService.VOTE_PARTY_ITEM);
                            player.sendMessage(ChatColor.GREEN + "You have been given the Vote Party Chest.");
                            break;
                        case "start":
                            if(!plugin.getData().getLocations(Data.VOTE_PARTY).isEmpty())
                            {
                                votePartyService.countdown();
                            } else
                            {
                                sender.sendMessage(ChatColor.RED + "There are no registered Vote Party Chests.");
                            }
                            break;
                        default:
                            sender.sendMessage(ChatColor.RED + "- /voteparty create | start");
                    }
                }
            } else
            {
                sender.sendMessage(plugin.getMessages().getMessage(Messages.MUST_BE_PLAYER));
            }
        } else
        {
            sender.sendMessage(plugin.getMessages().getMessage(Messages.NO_PERMISSION));
        }
        return false;
    }
}