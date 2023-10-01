package me.sd_master92.customvoting.constants.enumerations

import me.sd_master92.customvoting.CV
import org.bukkit.Material

enum class VMaterial(
    private val material_1_20_plus: String?,
    private val material_1_17_plus: String?,
    private val material_1_13_plus: String
)
{
    SOUL_TORCH(null, "SOUL_TORCH", Material.TORCH.name),
    SPYGLASS(null, "SPYGLASS", Material.LEAD.name),
    CRIMSON_SIGN(null, "CRIMSON_SIGN", Material.OAK_SIGN.name),
    SCULK_SENSOR( null, "SCULK_SENSOR", Material.REPEATER.name),
    PIG_HEAD("PIGLIN_HEAD", null, Material.PIG_SPAWN_EGG.name);

    fun get(): Material
    {
        return when
        {
            CV.MC_VERSION >= 20 ->
            {
                Material.valueOf(material_1_20_plus ?: material_1_17_plus ?: material_1_13_plus)
            }
            CV.MC_VERSION >= 17 ->
            {
                Material.valueOf(material_1_17_plus ?: material_1_13_plus)
            }

            else                ->
            {
                Material.valueOf(material_1_13_plus)
            }
        }
    }
}