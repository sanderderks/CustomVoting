package me.sd_master92.customvoting.commands;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.Messages;
import me.sd_master92.customvoting.constants.Sounds;
import me.sd_master92.customvoting.services.GUIService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class SettingsCommand implements CommandExecutor
{
    private final Main plugin;
    private final GUIService guiService;

    public SettingsCommand(Main plugin)
    {
        this.plugin = plugin;
        guiService = new GUIService(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(command.getPermission() != null && sender.hasPermission(command.getPermission()))
        {
            if (sender instanceof Player)
            {
                Player player = (Player) sender;
                Inventory settings = guiService.getSettings();
                Sounds.OPEN.play(plugin, player.getLocation());
                player.openInventory(settings);
            }
        } else
        {
            sender.sendMessage(plugin.getMessages().getMessage(Messages.NO_PERMISSION));
        }
        return true;
    }
}
