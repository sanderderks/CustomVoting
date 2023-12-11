package me.sd_master92.customvoting.constants.models

data class VoterData(
    val name: String,
    val votes: Int,
    val votesMonthly: Int,
    val votesWeekly: Int,
    val votesDaily: Int,
    val streakDaily: Int
)