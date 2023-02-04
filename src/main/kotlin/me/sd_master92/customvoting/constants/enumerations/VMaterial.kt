package me.sd_master92.customvoting.constants.enumerations

import me.sd_master92.customvoting.CV
import org.bukkit.Material

enum class VMaterial(
    private val material_1_17_plus: String,
    private val material_1_13_plus: String
)
{
    SOUL_TORCH("SOUL_TORCH", "TORCH"),
    SPYGLASS("SPYGLASS", "LEAD");

    fun get(): Material
    {
        return when
        {
            CV.MC_VERSION >= 17 ->
            {
                Material.valueOf(material_1_17_plus)
            }

            else                ->
            {
                Material.valueOf(material_1_13_plus)
            }
        }
    }
}