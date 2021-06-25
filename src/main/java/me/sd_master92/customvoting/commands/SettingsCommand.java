package me.sd_master92.customvoting.commands;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.enumerations.SoundType;
import me.sd_master92.customvoting.services.GUIService;
import me.sd_master92.plugin.command.SimpleCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class SettingsCommand extends SimpleCommand
{
    private final Main plugin;
    private final GUIService guiService;

    public SettingsCommand(Main plugin)
    {
        super(plugin, "votesettings");
        withPlayer();
        this.plugin = plugin;
        guiService = new GUIService(plugin);
    }

    @Override
    public void onCommand(CommandSender commandSender, String[] strings)
    {

    }

    @Override
    public void onCommand(Player player, String[] strings)
    {
        Inventory settings = guiService.getConfig();
        SoundType.OPEN.play(plugin, player);
        player.openInventory(settings);
    }
}
