package me.sd_master92.customvoting.extensions;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.VoteFile;
import me.sd_master92.customvoting.constants.Voter;
import me.sd_master92.customvoting.database.PlayerRow;
import me.sd_master92.customvoting.database.PlayerTable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CustomPlaceholders extends PlaceholderExpansion
{
    public static final String IDENTIFIER = "CV";
    public static final String SERVER_VOTES = "SERVER_VOTES";
    public static final String PLAYER_VOTES = "PLAYER_VOTES";
    private final Main plugin;

    public CustomPlaceholders(Main plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier()
    {
        return IDENTIFIER;
    }

    @Override
    public @NotNull String getAuthor()
    {
        return plugin.AUTHOR;
    }

    @Override
    public @NotNull String getVersion()
    {
        return plugin.VERSION;
    }

    @Override
    public boolean canRegister()
    {
        return true;
    }

    @Override
    public boolean persist()
    {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params)
    {
        try
        {
            if (params.equals(SERVER_VOTES))
            {
                int total = 0;
                for (Voter voter : plugin.hasDatabaseConnection() ? PlayerTable.getTopVoters(plugin) :
                        VoteFile.getTopVoters(plugin))
                {
                    total += voter.getVotes();
                }
                return "" + total;
            } else if (params.equals(PLAYER_VOTES))
            {
                Voter voter = plugin.hasDatabaseConnection() ? new PlayerRow(plugin, player) :
                        new VoteFile(player, plugin);
                return "" + voter.getVotes();
            } else if (params.contains(PLAYER_VOTES))
            {
                int key = Integer.parseInt(params.split("_")[2]);
                Voter topVoter = plugin.hasDatabaseConnection() ? PlayerTable.getTopVoter(plugin, key
                ) : VoteFile.getTopVoter(plugin, key);
                if (params.endsWith("NAME"))
                {
                    return topVoter == null ? "Unknown" : topVoter.getName();
                } else
                {
                    return topVoter == null ? "0" : "" + topVoter.getVotes();
                }
            }
        } catch (Exception e)
        {
            return null;
        }
        return null;
    }
}
