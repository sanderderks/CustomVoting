package me.sd_master92.customvoting.commands;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.enumerations.SoundType;
import me.sd_master92.customvoting.gui.VoteSettings;
import me.sd_master92.plugin.command.SimpleCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class SettingsCommand extends SimpleCommand
{
    private final Main plugin;

    public SettingsCommand(Main plugin)
    {
        super(plugin, "votesettings");
        withPlayer();
        this.plugin = plugin;
    }

    @Override
    public void onCommand(CommandSender commandSender, String[] strings)
    {

    }

    @Override
    public void onCommand(Player player, String[] strings)
    {
        Inventory settings = new VoteSettings().getInventory();
        SoundType.OPEN.play(plugin, player);
        player.openInventory(settings);
    }
}
