package me.sd_master92.customvoting.constants.enumerations

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.subjects.VoteSite

enum class Data(val path: String)
{
    VOTE_PARTY_CHESTS("vote_party"),
    ITEM_REWARDS("rewards"),
    LUCKY_REWARDS("lucky_rewards"),
    CURRENT_VOTES("current_votes"),
    VOTE_HISTORY("history"),
    VOTE_TOP_SIGNS("vote_top"),
    VOTE_TOP_STANDS("armor_stands"),
    VOTE_COMMANDS("vote_commands"),
    VOTE_PARTY_COMMANDS("vote_party_commands"),
    MILESTONES("milestones"),
    STREAKS("streaks"),
    VOTE_SITES("vote_sites"),
    VOTE_CRATES("vote_crates");

    override fun toString(): String
    {
        return path
    }

    companion object
    {
        const val POWER_REWARDS = "_op"
        val CRATE_REWARD_CHANCES = listOf(50, 25, 10, 5, 1)

        fun initialize(plugin: CV)
        {
            migrate(plugin)
        }

        private fun migrate(plugin: CV)
        {
            VoteSite.migrate(plugin)

            val keyMigrations = mapOf(
                Pair("vote_streaks", MILESTONES.path),
            )

            plugin.data.keyMigrations(keyMigrations)
            plugin.data.deleteLocation("armor_stands")
            plugin.data.deleteItems("vote_link_items")
            plugin.data.delete("vote_links")
            plugin.data.delete("queue")
        }
    }
}