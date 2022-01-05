package me.sd_master92.customvoting.constants

interface Voter
{
    val votes: Int
    val period: Int
    val uuid: String
    val name: String
}