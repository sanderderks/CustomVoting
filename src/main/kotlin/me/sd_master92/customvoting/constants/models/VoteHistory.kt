package me.sd_master92.customvoting.constants.models

data class VoteHistory(val id: Int, val uuid: String, val site: VoteSiteUUID, val time: Long, val queued: Boolean)