package me.sd_master92.customvoting.constants.enumerations

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.setLanguage
import java.util.*

enum class Language(val locale: Locale, val label: String)
{
    EN(Locale.ENGLISH, Locale.ENGLISH.getDisplayLanguage(Locale.ENGLISH) + "/" + Locale.ENGLISH.displayLanguage),
    DE(Locale.GERMAN, Locale.GERMAN.getDisplayLanguage(Locale.GERMAN) + "/" + Locale.GERMAN.displayLanguage),
    NL(Locale.of("nl"), Locale.of("nl").getDisplayLanguage(Locale.of("nl")) + "/" + Locale.of("nl").displayLanguage);

    fun switch(plugin: CV)
    {
        val language = if (ordinal < entries.size - 1)
        {
            valueOf(ordinal + 1)
        } else
        {
            valueOf(0)
        }
        plugin.config.setNumber(Setting.LANGUAGE.path, language.ordinal)
        language.locale.setLanguage()
    }

    companion object
    {
        fun get(plugin: CV): Language
        {
            return valueOf(plugin.config.getNumber(Setting.LANGUAGE.path))
        }

        fun valueOf(key: Int): Language
        {
            return try
            {
                entries[key]
            } catch (_: Exception)
            {
                entries[0]
            }
        }
    }
}