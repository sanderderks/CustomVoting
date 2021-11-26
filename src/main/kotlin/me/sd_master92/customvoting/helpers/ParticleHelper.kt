package me.sd_master92.customvoting.helpers

import me.sd_master92.customvoting.Main
import me.sd_master92.customvoting.constants.Settings
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Firework
import java.util.*

object ParticleHelper
{
    fun shootFirework(plugin: Main, loc: Location)
    {
        if (plugin.config.getBoolean(Settings.FIREWORK))
        {
            val world = loc.world
            if (world != null)
            {
                val firework = world.spawnEntity(loc, EntityType.FIREWORK) as Firework
                val fireworkMeta = firework.fireworkMeta
                val random = Random()
                val colors = arrayOf(
                    Color.AQUA, Color.BLUE, Color.FUCHSIA, Color.GREEN, Color.LIME, Color.MAROON,
                    Color.NAVY,
                    Color.OLIVE, Color.ORANGE, Color.PURPLE, Color.RED, Color.TEAL
                )
                val fireworkEffects = arrayOf(
                    FireworkEffect.Type.BALL, FireworkEffect.Type.BALL_LARGE,
                    FireworkEffect.Type.BURST, FireworkEffect.Type.STAR
                )
                val effect = FireworkEffect.builder()
                    .flicker(random.nextBoolean())
                    .withColor(colors[random.nextInt(colors.size)])
                    .withFade(colors[random.nextInt(colors.size)])
                    .with(fireworkEffects[random.nextInt(fireworkEffects.size)])
                    .trail(random.nextBoolean()).build()
                fireworkMeta.addEffect(effect)
                fireworkMeta.power = random.nextInt(2) + 1
                firework.fireworkMeta = fireworkMeta
            }
        }
    }
}