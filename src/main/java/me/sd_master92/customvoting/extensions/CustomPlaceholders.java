package me.sd_master92.customvoting.extensions;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.VoteFile;
import me.sd_master92.customvoting.constants.Voter;
import me.sd_master92.customvoting.database.PlayerTable;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class CustomPlaceholders extends PlaceholderExpansion
{
    public static final String IDENTIFIER = "CV";
    public static final String PLAYER_VOTES_PREFIX = "PLAYER_VOTES";
    private final Main plugin;

    public CustomPlaceholders(Main plugin)
    {
        this.plugin = plugin;
    }

    public static void setPlayerVotes(int top)
    {
        PlaceholderAPI.setPlaceholders(null,
                "%" + IDENTIFIER + "_" + CustomPlaceholders.PLAYER_VOTES_PREFIX + "_" + top + "%");
        PlaceholderAPI.setPlaceholders(null, "%" + IDENTIFIER + "_" + CustomPlaceholders.PLAYER_VOTES_PREFIX + "%");
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
    public String onRequest(OfflinePlayer player, @NotNull String params)
    {
        try
        {
            if (params.equals(PLAYER_VOTES_PREFIX))
            {
                int total = 0;
                for (Voter voter : plugin.hasDatabaseConnection() ? PlayerTable.getTopVoters(plugin) :
                        VoteFile.getTopVoters(plugin))
                {
                    total += voter.getVotes();
                }
                return "" + total;
            } else if (params.contains(PLAYER_VOTES_PREFIX + "_"))
            {
                int key = Integer.parseInt(params.split("_")[2]);
                Voter topVoter = plugin.hasDatabaseConnection() ? PlayerTable.getTopVoter(plugin, key
                ) : VoteFile.getTopVoter(plugin, key);
                if (topVoter != null)
                {
                    if (params.endsWith("NAME"))
                    {
                        return topVoter.getName();
                    } else
                    {
                        return "" + topVoter.getVotes();
                    }
                }
            }
        } catch (Exception e)
        {
            return null;
        }
        return null;
    }
}
