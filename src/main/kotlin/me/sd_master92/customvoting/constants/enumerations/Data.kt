package me.sd_master92.customvoting.constants.enumerations

import me.sd_master92.customvoting.CV

enum class Data(val path: String)
{
    VOTE_PARTY("vote_party"),
    ITEM_REWARDS("rewards"),
    LUCKY_REWARDS("lucky_rewards"),
    CURRENT_VOTES("current_votes"),
    VOTE_QUEUE("queue"),
    VOTE_TOP_SIGNS("vote_top"),
    VOTE_TOP_STANDS("armor_stands"),
    VOTE_COMMANDS("vote_commands"),
    VOTE_PARTY_COMMANDS("vote_party_commands"),
    VOTE_LINK_ITEMS("vote_link_items"),
    VOTE_LINKS("vote_links"),
    MILESTONES("milestones"),
    VOTE_SITES("vote_sites"),
    VOTE_CRATES("vote_crates");

    override fun toString(): String
    {
        return path
    }

    companion object
    {
        const val OP_REWARDS = "_op"
        val CRATE_REWARD_CHANCES = listOf(50, 25, 10, 5, 1)

        fun initialize(plugin: CV)
        {
            migrate(plugin)
        }

        private fun migrate(plugin: CV)
        {
            val keyMigrations = mapOf(
                Pair("vote_streaks", MILESTONES.path)
            )

            for (migration in keyMigrations)
            {
                if (plugin.data.contains(migration.key))
                {
                    plugin.data.set(migration.value, plugin.data.get(migration.key))
                    plugin.data.delete(migration.key)
                }
            }
            plugin.data.saveConfig()
        }
    }
}