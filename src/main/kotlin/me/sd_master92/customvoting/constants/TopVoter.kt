package me.sd_master92.customvoting.constants

import me.sd_master92.customvoting.CV

interface TopVoter
{
    fun getAll(plugin: CV): MutableList<Voter>
}