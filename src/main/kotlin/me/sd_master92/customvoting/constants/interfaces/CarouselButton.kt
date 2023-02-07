package me.sd_master92.customvoting.constants.interfaces

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.customvoting.CV

interface CarouselButton
{
    fun newInstance(plugin: CV): BaseItem
}