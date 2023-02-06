package me.sd_master92.customvoting.constants.interfaces

import me.sd_master92.customvoting.CV

interface TopVoter
{
    fun getAll(plugin: CV): MutableList<Voter>
}