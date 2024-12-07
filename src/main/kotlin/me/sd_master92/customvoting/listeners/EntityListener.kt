package me.sd_master92.customvoting.listeners

import me.sd_master92.core.tasks.TaskTimer
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Message
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.getEntityHealthString
import me.sd_master92.customvoting.helpers.ParticleHelper
import me.sd_master92.customvoting.splashPotion
import me.sd_master92.customvoting.subjects.voteparty.VoteParty
import me.sd_master92.customvoting.subjects.voteparty.VotePartyChest
import org.bukkit.Difficulty
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.*
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
            while (chest.isNotEmpty())
            {
                chest.dropRandomItem(event.entity.location)
            }
            event.drops.clear()
            chest.show(chest.loc!!)
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
    fun onEntityChangeBlock(event: EntityChangeBlockEvent)
    {
        val entity = event.entity
        val blockTypeFrom = event.block.type
        val blockTypeTo = event.to
        if (entity is FallingBlock
            && (listOf(Material.AIR, Material.FIRE, Material.WATER, Material.LAVA).contains(blockTypeFrom))
            && blockTypeTo == Material.OBSIDIAN
            && FALLING_LOCKED_CRATE != null
        )
        {
            event.isCancelled = true
            turnFallingBlockIntoCrate(entity.location)
        }
    }

    @EventHandler
    fun onEntitySpawn(event: EntityDropItemEvent)
    {
        val entity = event.entity
        if (entity is FallingBlock
            && event.itemDrop.itemStack.type == Material.OBSIDIAN
            && FALLING_LOCKED_CRATE != null
        )
        {
            event.isCancelled = true
            turnFallingBlockIntoCrate(entity.location.add(0.0, 1.0, 0.0))
        }
    }

    private fun turnFallingBlockIntoCrate(loc: Location)
    {
        val block = loc.block
        val blockLoc = block.location
        val chest = FALLING_LOCKED_CRATE!!
        chest.lockedCrateLoc = loc
        chest.show(blockLoc)
        blockLoc.world!!.spawnParticle(Particle.EXPLOSION_NORMAL, blockLoc, 3)
        SoundType.EXPLODE.play(plugin, blockLoc)
        PlayerListener.LOCKED_CRATES[blockLoc] = chest
        FALLING_LOCKED_CRATE = null
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
                    val maxDamage = plugin.config.getDouble(Setting.VOTE_PARTY_PIG_HUNT_DAMAGE_MAX.path)
                    event.damage = when (random.nextInt(6))
                    {
                        0    ->
                        {
                            event.entity.world.spawnParticle(Particle.SPELL, event.entity.location, 3)
                            maxDamage
                        }

                        1    -> maxDamage * 0.75
                        2    -> maxDamage * 0.5
                        3    ->
                        {
                            SoundType.FUNNY_2.play(plugin, event.entity.location)
                            0.0
                        }

                        else -> maxDamage * 0.25
                    }

                    when (random.nextInt(10))
                    {
                        0    ->
                        {
                            event.entity.location.splashPotion(Material.SPLASH_POTION, PotionEffectType.HEAL)
                            event.damage = maxDamage * -0.25
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
                                var entity = event.entity
                                while (entity.isInsideVehicle)
                                {
                                    entity = entity.vehicle!!
                                }
                                val pig = entity.world.spawnEntity(entity.location, EntityType.PIG) as Pig
                                pig.addPassenger(entity)
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
        var FALLING_LOCKED_CRATE: VotePartyChest? = null
    }
}