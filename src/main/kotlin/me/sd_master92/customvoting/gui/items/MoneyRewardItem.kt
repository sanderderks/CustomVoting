package me.sd_master92.customvoting.gui.items

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.Settings
import org.bukkit.ChatColor
import org.bukkit.Material

class MoneyRewardItem private constructor(plugin: CV, path: String) : BaseItem(
    Material.GOLD_INGOT, ChatColor.LIGHT_PURPLE.toString() + "Money Reward",
    if (CV.ECONOMY != null) ChatColor.GRAY.toString() + "Currently: " + ChatColor.GREEN + CV.ECONOMY!!.format(
        plugin.config.getDouble(path)
    ) else ChatColor.RED.toString() + "Disabled"
)
{
    companion object
    {
        fun getInstance(plugin: CV, op: Boolean): MoneyRewardItem
        {
            var path = Settings.VOTE_REWARD_MONEY
            if (op)
            {
                path += Data.OP_REWARDS
            }
            return MoneyRewardItem(plugin, path)
        }
    }
}