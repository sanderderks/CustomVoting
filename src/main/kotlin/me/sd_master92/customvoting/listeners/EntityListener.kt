package me.sd_master92.customvoting.listeners

import me.sd_master92.core.tasks.TaskTimer
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Message
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.getEntityHealthString
import me.sd_master92.customvoting.helpers.ParticleHelper
import me.sd_master92.customvoting.splashPotion
import me.sd_master92.customvoting.subjects.voteparty.VoteParty
import me.sd_master92.customvoting.subjects.voteparty.VotePartyChest
import org.bukkit.Difficulty
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityTargetLivingEntityEvent
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.potion.PotionEffectType
import java.util.*

class EntityListener(private val plugin: CV) : Listener
{
    private val random: Random = Random()

    @EventHandler
    fun onEntityAttack(event: EntityTargetLivingEntityEvent)
    {
        if (event.entity.uniqueId in CANCEL_EVENT)
        {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent)
    {
        val uuid = event.entity.uniqueId
        if (uuid in PIG_HUNT)
        {
            val chest = PIG_HUNT[uuid]!!
            chest.dropLoc = event.entity.location
            while (chest.isNotEmpty())
            {
                chest.dropRandomItem()
            }
            event.drops.clear()
            chest.show()
            PIG_HUNT.remove(uuid)
            ParticleHelper.shootFirework(plugin, event.entity.location)

            val placeholders = HashMap<String, String>()
            placeholders["%KILLER%"] = event.entity.killer?.name ?: event.entity.type.toString()
            if (PIG_HUNT.isNotEmpty())
            {
                placeholders["%COUNT%"] = "" + (VotePartyChest.getAll(plugin).size - PIG_HUNT.size)
                placeholders["%TOGO%"] = "" + PIG_HUNT.size
                placeholders["%s%"] = if (PIG_HUNT.size == 1) "" else "s"
                plugin.broadcastText(Message.VOTE_PARTY_PIG_KILLED, placeholders)
            } else
            {
                plugin.broadcastText(Message.VOTE_PARTY_PIG_KILLED_LAST, placeholders)
                VoteParty.stop(plugin)
            }
        }
    }

    @EventHandler
    fun onEntityInteract(event: PlayerInteractAtEntityEvent)
    {
        if (event.rightClicked.uniqueId in PIG_HUNT)
        {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onEntityDamage(event: EntityDamageByEntityEvent)
    {
        if (event.damager is Player)
        {
            onEntityDamageByPlayer(event, false)
        } else if (event.damager is Projectile && (event.damager as Projectile).shooter is Player)
        {
            onEntityDamageByPlayer(event, true)
        }
    }

    private fun onEntityDamageByPlayer(event: EntityDamageByEntityEvent, isIndirect: Boolean)
    {
        if (event.entity.uniqueId in PIG_HUNT.keys)
        {
            if (!event.entity.isInvulnerable)
            {
                if (isIndirect)
                {
                    event.isCancelled = true
                } else
                {
                    event.damage = when (random.nextInt(6))
                    {
                        0    ->
                        {
                            event.entity.world.spawnParticle(Particle.SPELL, event.entity.location, 3)
                            4.0
                        }

                        1    -> 3.0
                        2    -> 2.0
                        3    ->
                        {
                            SoundType.FUNNY_2.play(plugin, event.entity.location)
                            0.0
                        }

                        else -> 1.0
                    }

                    when (random.nextInt(10))
                    {
                        0    ->
                        {
                            event.entity.location.splashPotion(Material.SPLASH_POTION, PotionEffectType.HEAL)
                            event.damage = -1.0
                        }

                        1, 2 ->
                        {
                            event.entity.world.spawnParticle(Particle.EXPLOSION_HUGE, event.entity.location, 1)
                            SoundType.EXPLODE.play(plugin, event.entity.location)
                        }

                        3, 4 ->
                        {
                            event.entity.world.spawnParticle(Particle.HEART, event.entity.location, 3)
                            SoundType.FUNNY_1.play(plugin, event.entity.location)
                        }

                        5    ->
                        {
                            event.entity.fireTicks = 10
                        }

                        6    ->
                        {
                            if (event.entity.world.difficulty == Difficulty.PEACEFUL || random.nextInt(2) == 1)
                            {
                                val pig = event.entity.world.spawnEntity(event.entity.location, EntityType.PIG) as Pig
                                pig.addPassenger(event.entity)
                            } else if (random.nextInt(2) == 1)
                            {
                                val creeper =
                                    event.entity.world.spawnEntity(event.entity.location, EntityType.CREEPER) as Creeper
                                creeper.explosionRadius = 0
                                creeper.isPowered = true
                                creeper.target = event.damager as LivingEntity
                                TaskTimer.delay(plugin, 20 * 10)
                                {
                                    creeper.remove()
                                }
                            } else
                            {
                                val flying = event.entity.world.spawnEntity(
                                    event.entity.location,
                                    if (CV.MC_VERSION >= 19) EntityType.valueOf("ALLAY") else EntityType.BAT
                                )
                                flying.addPassenger(event.entity)
                                TaskTimer.delay(plugin, 20 * 5)
                                {
                                    flying.remove()
                                }.run()
                            }
                        }

                        7    ->
                        {
                            SoundType.SURPRISE.play(plugin, event.entity.location)
                            event.entity.isGlowing = true
                            TaskTimer.delay(plugin, 20 * 3)
                            {
                                event.entity.isGlowing = false
                            }.run()
                        }

                        else ->
                        {
                            ParticleHelper.shootFirework(plugin, event.entity.location.add(0.0, 1.0, 0.0))
                        }
                    }
                }
                if (random.nextInt(2) == 1)
                {
                    event.damager.velocity = event.damager.location.direction.multiply(-2)
                }

                TaskTimer.delay(plugin, 1)
                {
                    event.entity.customName = (event.entity as LivingEntity).getEntityHealthString()
                }.run()
            } else
            {
                event.isCancelled = true
            }
        }
    }

    companion object
    {
        val CANCEL_EVENT = mutableListOf<UUID>()
        val PIG_HUNT = mutableMapOf<UUID, VotePartyChest>()
    }
}