package me.sd_master92.customvoting.database

import me.sd_master92.customvoting.constants.Voter

class PlayerData(
    override val uuid: String,
    override val name: String,
    override val votes: Int,
    val last: Long,
    val queue: Int,
    override val period: Int
) : Voter