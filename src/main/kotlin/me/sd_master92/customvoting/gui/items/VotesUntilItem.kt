package me.sd_master92.customvoting.gui.items

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.Settings
import org.bukkit.ChatColor
import org.bukkit.Material

class VotesUntilItem private constructor(votesRequired: Int, votesUntil: Int) : BaseItem(
    Material.ENCHANTED_BOOK, ChatColor.LIGHT_PURPLE.toString() + "Votes until Vote Party",
    ChatColor.GRAY.toString() + "Required: " + ChatColor.AQUA + votesRequired + ";" + ChatColor.GRAY + "Votes left:" +
            " " + ChatColor.GREEN + votesUntil
)
{
    companion object
    {
        fun getInstance(plugin: CV): VotesUntilItem
        {
            val votesRequired = plugin.config.getNumber(Settings.VOTES_REQUIRED_FOR_VOTE_PARTY)
            val votesUntil = votesRequired - plugin.data.getNumber(Data.CURRENT_VOTES)
            return VotesUntilItem(votesRequired, votesUntil)
        }
    }
}