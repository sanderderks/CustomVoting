package me.sd_master92.customvoting;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import me.sd_master92.customfile.CustomFile;
import me.sd_master92.customfile.PlayerFile;
import me.sd_master92.customvoting.listeners.VotifierListener;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;

public class Main extends JavaPlugin
{
    public final String NAME = getDescription().getName();
    public final String VERSION = getDescription().getVersion();
    public final String AUTHOR = getDescription().getAuthors().get(0);
    public final int SPIGOT = 28103;

    public CustomFile config;
    public CustomFile messages;
    public PlayerFile players;

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

        Vote vote = new Vote();
        vote.setUsername("test");
        vote.setServiceName("fakevote.com");
        vote.setAddress("0.0.0.0");
        Date date = new Date();
        vote.setTimeStamp(String.valueOf(date.getTime()));
        getServer().getPluginManager().callEvent(new VotifierEvent(vote));

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
            error("|___Dependency 'Votifier' not found. Disabling...");
            setEnabled(false);
            return false;
        }
        print("|___Dependency 'Votifier' found!");
        return true;
    }

    private void registerListeners()
    {
        getServer().getPluginManager().registerEvents(new VotifierListener(this, getPlayers()), this);
    }

    private void registerCommands()
    {
        // register commands
    }

    private void registerFiles()
    {
        config = new CustomFile("config.yml", this);
        messages = new CustomFile("messages.yml", this);
        players = new PlayerFile("players.yml", this);
    }

    public CustomFile getSettings()
    {
        return config;
    }

    public CustomFile getMessages()
    {
        return messages;
    }

    public PlayerFile getPlayers()
    {
        return players;
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
