package me.sd_master92.customvoting.constants

interface Voter
{
    val votes: Int
    val monthlyVotes: Int
    val uuid: String
    val name: String
    val last: Long
}