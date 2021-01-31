package me.sd_master92.customvoting.commands;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.types.Messages;
import me.sd_master92.customvoting.constants.types.SoundType;
import me.sd_master92.customvoting.helpers.SoundHelper;
import me.sd_master92.customvoting.services.GUIService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class SettingsCommand implements CommandExecutor
{
    private final GUIService guiService;

    public SettingsCommand(Main plugin)
    {
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
                SoundHelper.playSound(SoundType.OPEN, player.getLocation());
                player.openInventory(settings);
            }
        } else
        {
            sender.sendMessage(Messages.NO_PERMISSION);
        }
        return true;
    }
}
