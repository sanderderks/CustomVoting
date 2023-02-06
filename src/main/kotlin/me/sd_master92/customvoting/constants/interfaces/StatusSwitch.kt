package me.sd_master92.customvoting.constants.interfaces

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractStatusSwitch

interface StatusSwitch
{
    fun newInstance(plugin: CV): AbstractStatusSwitch
}