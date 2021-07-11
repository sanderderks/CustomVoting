package me.sd_master92.customvoting;

import me.sd_master92.customfile.CustomFile;
import me.sd_master92.customvoting.commands.*;
import me.sd_master92.customvoting.commands.voteparty.VotePartyCommand;
import me.sd_master92.customvoting.constants.Settings;
import me.sd_master92.customvoting.listeners.InventoryListener;
import me.sd_master92.customvoting.listeners.PlayerListener;
import me.sd_master92.customvoting.listeners.VoteTopListener;
import me.sd_master92.customvoting.listeners.VotifierListener;
import me.sd_master92.customvoting.tasks.DailyTask;
import me.sd_master92.plugin.CustomPlugin;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

public class Main extends CustomPlugin
{
    public static Economy economy = null;
    private CustomFile messages;
    private CustomFile data;

    public Main()
    {
        super(28103, "settings.yml");
    }

    @Override
    protected void enable()
    {
        if (!checkVotifier())
        {
            return;
        }
        checkHooks();
        registerFiles();
        registerListeners();
        registerCommands();
        startTasks();
    }

    @Override
    protected void disable()
    {

    }

    private boolean checkVotifier()
    {
        print("");
        print("| checking for Votifier");
        print("|");
        try
        {
            Class.forName("com.vexsoftware.votifier.model.VotifierEvent");
            print("|___dependency 'Votifier' found!");
            return true;
        } catch (ClassNotFoundException e)
        {
            error("|___dependency 'Votifier' not found, disabling...");
            setEnabled(false);
            return false;
        }
    }

    private void checkHooks()
    {
        print("");
        print("| checking for economy hook");
        print("|");
        if (!setupEconomy())
        {
            error("|___economy hook not found");
            return;
        }
        print("|___successfully hooked into '" + economy.getName() + "'");
    }

    private boolean setupEconomy()
    {
        if (getServer().getPluginManager().getPlugin("Vault") == null)
        {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
        {
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }

    private void registerFiles()
    {
        messages = new CustomFile("messages.yml", this);
        data = new CustomFile("data.yml", this);

        Settings.initialize(this);
    }

    public CustomFile getMessages()
    {
        return messages;
    }

    public CustomFile getData()
    {
        return data;
    }

    private void registerListeners()
    {
        registerListener(new PlayerListener(this));
        registerListener(new VotifierListener(this));
        registerListener(new VoteTopListener(this));
        registerListener(new InventoryListener(this));
    }

    private void registerCommands()
    {
        new CreateTopCommand(this);
        new DeleteTopCommand(this);
        new FakeVoteCommand(this);
        new ReloadCommand(this);
        new SettingsCommand(this);
        new SetVotesCommand(this);
        new VoteCommand(this);
        new VotePartyCommand(this);
        new VotesCommand(this);
        new VoteTopCommand(this);
    }

    private void startTasks()
    {
        new DailyTask(this);
    }
}
