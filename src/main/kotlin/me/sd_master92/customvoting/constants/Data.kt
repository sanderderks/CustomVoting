package me.sd_master92.customvoting.constants

import me.sd_master92.customvoting.CV

object Data
{
    const val VOTE_PARTY = "vote_party"
    const val OP_REWARDS = "_op"
    const val ITEM_REWARDS = "rewards"
    const val LUCKY_REWARDS = "lucky_rewards"
    const val CURRENT_VOTES = "current_votes"
    const val VOTE_QUEUE = "queue"
    const val VOTE_TOP_SIGNS = "vote_top"
    const val VOTE_TOP_STANDS = "armor_stands"
    const val VOTE_COMMANDS = "vote_commands"
    const val VOTE_PARTY_COMMANDS = "vote_party_commands"
    const val VOTE_LINK_ITEMS = "vote_link_items"
    const val VOTE_LINKS = "vote_links"
    const val MILESTONES = "milestones"
    const val VOTE_SITES = "vote_sites"
    const val VOTE_CRATES = "vote_crates"
    val CRATE_REWARD_CHANCES = listOf(50, 25, 10, 5, 1)

    fun initialize(plugin: CV)
    {
        migrate(plugin)
    }

    private fun migrate(plugin: CV)
    {
        val keyMigrations = mapOf(
            Pair("vote_streaks", MILESTONES)
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