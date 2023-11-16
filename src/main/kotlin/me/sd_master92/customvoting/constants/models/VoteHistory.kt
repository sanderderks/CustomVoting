package me.sd_master92.customvoting.constants.models

import java.util.*

data class VoteHistory(val id: Int, val uuid: UUID, val site: VoteSiteUUID, val time: Long, val queued: Boolean)