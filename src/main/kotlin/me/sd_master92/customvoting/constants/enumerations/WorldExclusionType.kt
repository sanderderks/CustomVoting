package me.sd_master92.customvoting.constants.enumerations

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.interfaces.CarouselEnum
import me.sd_master92.customvoting.constants.interfaces.EnumCompanion
import java.util.*

enum class WorldExclusionType(private val label_: PMessage) : CarouselEnum
{
    BLACKLIST(PMessage.GENERAL_VALUE_BLACKLIST),
    WHITELIST(PMessage.GENERAL_VALUE_WHITELIST);

    fun label(locale: Locale? = null): String
    {
        return label_.getValue(locale)
    }

    override fun next(): WorldExclusionType
    {
        return if (ordinal < VotePartyType.entries.size - 1)
        {
            WorldExclusionType.valueOf(ordinal + 1)
        } else
        {
            WorldExclusionType.valueOf(0)
        }
    }

    companion object : EnumCompanion
    {
        override fun valueOf(key: Int): WorldExclusionType
        {
            return try
            {
                entries[key]
            } catch (_: Exception)
            {
                entries[0]
            }
        }

        fun getCurrentValue(plugin: CV): WorldExclusionType
        {
            return valueOf(plugin.config.getNumber(Setting.WORLD_EXCLUSION_TYPE.path))
        }

        fun toggleValue(plugin: CV)
        {
            val currentValue = getCurrentValue(plugin)
            plugin.config.setNumber(
                Setting.WORLD_EXCLUSION_TYPE.path,
                currentValue.next().ordinal
            )
        }
    }
}