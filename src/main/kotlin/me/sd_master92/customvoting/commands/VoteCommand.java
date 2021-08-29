package me.sd_master92.customvoting.commands;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.Messages;
import me.sd_master92.customvoting.constants.Settings;
import me.sd_master92.customvoting.gui.VoteLinks;
import me.sd_master92.plugin.command.SimpleCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VoteCommand extends SimpleCommand
{
    private final Main plugin;

    public VoteCommand(Main plugin)
    {
        super(plugin, "vote");
        withPlayer();

        this.plugin = plugin;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args)
    {
    }

    @Override
    public void onCommand(Player player, String[] args)
    {
        if (plugin.getConfig().getBoolean(Settings.VOTE_LINK_INVENTORY))
        {
            player.openInventory(new VoteLinks(plugin, true).getInventory());
        } else
        {
            for (String message : Messages.VOTE_COMMAND.getMessages(plugin))
            {
                player.sendMessage(message);
            }
        }
    }
}