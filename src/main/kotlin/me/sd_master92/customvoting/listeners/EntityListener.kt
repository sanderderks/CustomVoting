package me.sd_master92.customvoting.listeners

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Messages
import me.sd_master92.customvoting.helpers.ParticleHelper
import me.sd_master92.customvoting.subjects.voteparty.VoteParty
import me.sd_master92.customvoting.subjects.voteparty.VotePartyChest
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityTargetLivingEntityEvent
import org.bukkit.event.player.PlayerInteractAtEntityEvent
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

            if (PIG_HUNT.isEmpty())
            {
                VoteParty.stop(plugin)
            } else
            {
                val placeholders = HashMap<String, String>()
                placeholders["%COUNT%"] = "" + (VotePartyChest.getAll(plugin).size - PIG_HUNT.size)
                placeholders["%TOGO%"] = "" + PIG_HUNT.size
                placeholders["%KILLER%"] = event.entity.killer?.name ?: event.entity.type.toString()
                placeholders["%s%"] = if (PIG_HUNT.size == 1) "" else "s"
                plugin.broadcastText(Messages.VOTE_PARTY_PIG_KILLED, placeholders)
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
                if (random.nextInt(2) == 1)
                {
                    if (isIndirect)
                    {
                        event.isCancelled = true
                    } else
                    {
                        event.damage = event.damage / 10
                    }
                    event.damager.velocity = event.damager.location.direction.multiply(-2)
                    ParticleHelper.shootFirework(plugin, event.entity.location.add(0.0, 1.0, 0.0))
                }
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