package me.sd_master92.customvoting.constants.enumerations

import me.sd_master92.customvoting.CV
import org.bukkit.Material

enum class VMaterial(
    private val material_1_17_plus: String,
    private val material_1_13_plus: String
)
{
    SOUL_TORCH("SOUL_TORCH", Material.TORCH.name),
    SPYGLASS("SPYGLASS", Material.LEAD.name),
    CRIMSON_SIGN("CRIMSON_SIGN", Material.OAK_SIGN.name),
    SCULK_SENSOR("SCULK_SENSOR", Material.REPEATER.name);

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