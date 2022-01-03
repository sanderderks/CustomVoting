package me.sd_master92.customvoting.constants

interface Voter
{
    val votes: Int
    val period: Int
    val uniqueId: String
    val userName: String
}