package me.sd_master92.customvoting.subjects

import me.sd_master92.customvoting.Main
import me.sd_master92.customvoting.VoteFile
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.Messages
import me.sd_master92.customvoting.database.PlayerTable
import me.sd_master92.customvoting.gui.GUI
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class VoteTopStand @JvmOverloads constructor(private val plugin: Main, private val top: Int, player: Player? = null)
{
    private var topStand: ArmorStand? = null
    private var nameStand: ArmorStand? = null
    private var votesStand: ArmorStand? = null
    private fun getArmorStand(uuid: String?): ArmorStand?
    {
        if (uuid != null)
        {
            val entity = plugin.server.getEntity(UUID.fromString(uuid))
            if (entity is ArmorStand)
            {
                return entity
            }
        }
        return null
    }

    private fun create(player: Player)
    {
        val world = player.location.world
        if (world != null)
        {
            topStand = world.spawnEntity(player.location.add(0.0, 1.0, 0.0),
                    EntityType.ARMOR_STAND) as ArmorStand
            plugin.data[Data.VOTE_TOP_STANDS + "." + top + ".top"] = topStand!!.uniqueId.toString()
            topStand!!.isVisible = false
            topStand!!.removeWhenFarAway = false
            topStand!!.isSilent = true
            topStand!!.isPersistent = true
            topStand!!.setGravity(false)
            topStand!!.isCustomNameVisible = true
            nameStand = world.spawnEntity(player.location.add(0.0, 0.5, 0.0),
                    EntityType.ARMOR_STAND) as ArmorStand
            plugin.data[Data.VOTE_TOP_STANDS + "." + top + ".name"] = nameStand!!.uniqueId.toString()
            nameStand!!.isVisible = false
            nameStand!!.removeWhenFarAway = false
            nameStand!!.isSilent = true
            nameStand!!.isPersistent = true
            nameStand!!.setGravity(false)
            nameStand!!.isCustomNameVisible = true
            votesStand = world.spawnEntity(player.location, EntityType.ARMOR_STAND) as ArmorStand
            plugin.data[Data.VOTE_TOP_STANDS + "." + top + ".votes"] = votesStand!!.uniqueId.toString()
            plugin.data.saveConfig()
            votesStand!!.removeWhenFarAway = false
            votesStand!!.isSilent = true
            votesStand!!.isPersistent = true
            votesStand!!.setGravity(false)
            votesStand!!.isCustomNameVisible = true
            votesStand!!.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING)
            votesStand!!.addEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.REMOVING_OR_CHANGING)
            votesStand!!.addEquipmentLock(EquipmentSlot.LEGS, ArmorStand.LockType.REMOVING_OR_CHANGING)
            votesStand!!.addEquipmentLock(EquipmentSlot.FEET, ArmorStand.LockType.REMOVING_OR_CHANGING)
            votesStand!!.addEquipmentLock(EquipmentSlot.HAND, ArmorStand.LockType.REMOVING_OR_CHANGING)
            val entityEquipment = votesStand!!.equipment
            if (entityEquipment != null)
            {
                when (top)
                {
                    1 ->
                    {
                        entityEquipment.chestplate = GUI.createItem(Material.DIAMOND_CHESTPLATE, true)
                        entityEquipment.leggings = GUI.createItem(Material.DIAMOND_LEGGINGS, true)
                        entityEquipment.boots = GUI.createItem(Material.DIAMOND_BOOTS, true)
                        entityEquipment.setItemInMainHand(GUI.createItem(Material.DIAMOND_SWORD,
                                true))
                    }
                    2 ->
                    {
                        entityEquipment.chestplate = GUI.createItem(Material.GOLDEN_CHESTPLATE, true)
                        entityEquipment.leggings = GUI.createItem(Material.GOLDEN_LEGGINGS, true)
                        entityEquipment.boots = GUI.createItem(Material.GOLDEN_BOOTS, true)
                        entityEquipment.setItemInMainHand(GUI.createItem(Material.GOLDEN_SWORD,
                                true))
                    }
                    3 ->
                    {
                        entityEquipment.chestplate = GUI.createItem(Material.IRON_CHESTPLATE, true)
                        entityEquipment.leggings = GUI.createItem(Material.IRON_LEGGINGS, true)
                        entityEquipment.boots = GUI.createItem(Material.IRON_BOOTS, true)
                        entityEquipment.setItemInMainHand(GUI.createItem(Material.IRON_SWORD,
                                true))
                    }
                    else ->
                    {
                        entityEquipment.chestplate = GUI.createItem(Material.CHAINMAIL_CHESTPLATE, true)
                        entityEquipment.leggings = GUI.createItem(Material.CHAINMAIL_LEGGINGS, true)
                        entityEquipment.boots = GUI.createItem(Material.CHAINMAIL_BOOTS, true)
                        entityEquipment.setItemInMainHand(GUI.createItem(Material.STONE_SWORD, true))
                    }
                }
            }
        }
        player.sendMessage(ChatColor.GREEN.toString() + "Registered Vote Stand #" + top)
    }

    private fun update()
    {
        val voteFile = if (plugin.hasDatabaseConnection()) PlayerTable.getTopVoter(plugin, top) else VoteFile.getTopVoter(plugin,
                top)
        val placeholders: MutableMap<String, String> = HashMap()
        placeholders["%TOP%"] = "" + top
        if (voteFile != null)
        {
            placeholders["%PLAYER%"] = voteFile.userName
            placeholders["%VOTES%"] = "" + voteFile.votes
        } else
        {
            placeholders["%PLAYER%"] = ChatColor.RED.toString() + "Unknown"
            placeholders["%VOTES%"] = "" + 0
        }
        topStand!!.customName = Messages.VOTE_TOP_STANDS_TOP.getMessage(plugin, placeholders)
        nameStand!!.customName = Messages.VOTE_TOP_STANDS_CENTER.getMessage(plugin, placeholders)
        votesStand!!.customName = Messages.VOTE_TOP_STANDS_BOTTOM.getMessage(plugin, placeholders)
        val skull = ItemStack(Material.PLAYER_HEAD)
        val skullMeta = skull.itemMeta as SkullMeta?
        if (skullMeta != null && voteFile != null)
        {
            try
            {
                skullMeta.owningPlayer = Bukkit.getOfflinePlayer(UUID.fromString(voteFile.uniqueId))
                skull.itemMeta = skullMeta
            } catch (ignored: Exception)
            {
            }
        }
        val entityEquipment = votesStand!!.equipment
        if (entityEquipment != null)
        {
            entityEquipment.helmet = skull
        }
    }

    fun delete(player: Player)
    {
        topStand!!.remove()
        nameStand!!.remove()
        votesStand!!.remove()
        plugin.data[Data.VOTE_TOP_STANDS + "." + top] = null
        plugin.data.saveConfig()
        voteTops.remove(top)
        player.sendMessage(ChatColor.GREEN.toString() + "Successfully deleted Vote Stand #" + top)
    }

    companion object
    {
        private val voteTops: MutableMap<Int, VoteTopStand> = HashMap()
        operator fun get(top: Int): VoteTopStand?
        {
            return voteTops[top]
        }

        fun updateAll(plugin: Main)
        {
            if (voteTops.isEmpty())
            {
                initialize(plugin)
            }
            object : BukkitRunnable()
            {
                override fun run()
                {
                    for (voteTop in voteTops.values)
                    {
                        voteTop.update()
                    }
                }
            }.runTaskLater(plugin, 40L)
        }

        private fun initialize(plugin: Main)
        {
            val section = plugin.data.getConfigurationSection(Data.VOTE_TOP_STANDS)
            if (section != null)
            {
                for (n in section.getKeys(false))
                {
                    try
                    {
                        val top = n.toInt()
                        VoteTopStand(plugin, top)
                    } catch (ignored: Exception)
                    {
                    }
                }
            }
        }
    }

    init
    {
        val section = plugin.data.getConfigurationSection(Data.VOTE_TOP_STANDS + "." + top)
        if (section != null)
        {
            if (player != null)
            {
                player.sendMessage(ChatColor.RED.toString() + "That Vote Stand already exists.")
            } else
            {
                topStand = getArmorStand(section.getString("top"))
                nameStand = getArmorStand(section.getString("name"))
                votesStand = getArmorStand(section.getString("votes"))
                voteTops[top] = this
                update()
            }
        } else player?.let {
            create(it)
            voteTops[top] = this
            update()
        }
    }
}