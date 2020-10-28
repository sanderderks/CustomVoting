package me.sd_master92.customvoting;

import me.sd_master92.customfile.CustomFile;
import me.sd_master92.customvoting.commands.*;
import me.sd_master92.customvoting.listeners.InventoryListener;
import me.sd_master92.customvoting.listeners.PlayerListener;
import me.sd_master92.customvoting.listeners.VoteTopListener;
import me.sd_master92.customvoting.listeners.VotifierListener;
import me.sd_master92.customvoting.services.GUIService;
import me.sd_master92.customvoting.services.VoteService;
import me.sd_master92.customvoting.services.VoteTopService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;

public class Main extends JavaPlugin
{
    private final String NAME = getDescription().getName();
    private final String VERSION = getDescription().getVersion();
    private final String AUTHOR = getDescription().getAuthors().get(0);

    private CustomFile config;
    private CustomFile messages;
    private CustomFile data;

    private VoteService voteService;
    private VoteTopService voteTopService;
    private GUIService guiService;

    @Override
    public void onEnable()
    {
        print("");
        print("                      " + NAME);
        print(">----------------------------------------------------");
        print("                     By " + AUTHOR);

        checkUpdates();
        if (!checkDependencies())
        {
            return;
        }
        registerFiles();
        registerListeners();
        registerCommands();
        registerServices();

        print("");
        print(ChatColor.GREEN + "v" + VERSION + " has been enabled.");
        print("");
        print(">----------------------------------------------------");
    }

    @Override
    public void onDisable()
    {
        print("");
        print(ChatColor.RED + "v" + VERSION + " has been disabled.");
        print("");
        print(">----------------------------------------------------");
    }

    private void checkUpdates()
    {
        print("");
        print("| checking for updates");
        print("|");
        int SPIGOT = 28103;
        String version;
        try
        {
            HttpsURLConnection connection = (HttpsURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=" + SPIGOT).openConnection();
            connection.setRequestMethod("GET");
            version = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
        } catch (Exception e)
        {
            version = null;
        }
        if (version != null)
        {
            if (VERSION.equalsIgnoreCase(version))
            {
                print("|___up to date!");
            } else
            {
                error("|   a new version is available");
                error("|   download " + NAME + " v" + version + " at:");
                error("|___https://www.spigotmc.org/resources/" + SPIGOT + "/");
            }
        } else
        {
            error("|___could not check for updates");
        }
    }

    private boolean checkDependencies()
    {
        print("");
        print("| checking for Votifier");
        print("|");
        Plugin votifier = getServer().getPluginManager().getPlugin("Votifier");
        if (votifier == null)
        {
            error("|___dependency 'Votifier' not found, disabling...");
            setEnabled(false);
            return false;
        }
        print("|___dependency 'Votifier' found!");
        return true;
    }

    private void registerFiles()
    {
        config = new CustomFile("config.yml", this);
        messages = new CustomFile("messages.yml", this);
        data = new CustomFile("data.yml", this);
    }

    public CustomFile getSettings()
    {
        return config;
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

    private void registerListener(Listener listener)
    {
        getServer().getPluginManager().registerEvents(listener,this);
    }

    private void registerCommands()
    {
        registerCommand("vote", new VoteCommand(this));
        registerCommand("votes", new VotesCommand(this));
        registerCommand("votetop", new VoteTopCommand(this));
        registerCommand("fakevote", new FakeVoteCommand(this));
        registerCommand("setvotes", new SetVotesCommand(this));

        registerCommand("votereload", new ReloadCommand(this));
        registerCommand("votesettings", new SettingsCommand(this));
    }

    private void registerCommand(String name, CommandExecutor executor)
    {
        try
        {
            Objects.requireNonNull(getCommand(name.toLowerCase())).setExecutor(executor);
        } catch (Exception e)
        {
            print("");
            print("|");
            error("|___could not register command '" + name.toLowerCase() + "'!");
        }
    }

    private void registerServices()
    {
        voteService = new VoteService(this);
        voteTopService = new VoteTopService(this);
        guiService = new GUIService(this);
    }

    public VoteService getVoteService()
    {
        return voteService;
    }

    public VoteTopService getVoteTopService()
    {
        return voteTopService;
    }

    public GUIService getGuiService()
    {
        return guiService;
    }

    public void print(String message)
    {
        getServer().getConsoleSender().sendMessage("[" + NAME + "] " + message);
    }

    public void error(String message)
    {
        getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[" + NAME + "] " + ChatColor.RESET + message);
    }
}
