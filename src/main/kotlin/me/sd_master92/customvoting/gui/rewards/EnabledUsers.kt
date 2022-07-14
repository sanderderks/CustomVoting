package me.sd_master92.customvoting.gui.rewards

import me.sd_master92.core.file.PlayerFile
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.VoteFile
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.getSkull
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class EnabledUsers(private val plugin: CV) :
    GUI(plugin, "Enabled Users", 54, false, true)
{
    override fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)
    {
        when (item.type)
        {
            Material.BARRIER     ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent()
                player.openInventory(RewardSettings(plugin, true).inventory)
            }
            Material.PLAYER_HEAD ->
            {
                SoundType.CHANGE.play(plugin, player)
                var name = ChatColor.stripColor(item.itemMeta?.displayName)
                if (name == null)
                {
                    name = ""
                }
                val playerFile = PlayerFile.getByName(name)
                if (playerFile != null)
                {
                    val voteFile = VoteFile(playerFile.uuid, plugin)
                    voteFile.setIsOpUser(!voteFile.isOpUser)
                    event.currentItem = EnabledUser(voteFile).getSkull()
                } else
                {
                    event.currentItem = null
                }
            }
            else                 ->
            {
            }
        }
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        SoundType.CLOSE.play(plugin, player)
    }

    init
    {
        for (uuid in PlayerFile.getAll().keys)
        {
            inventory.addItem(EnabledUser(VoteFile(uuid, plugin)).getSkull())
        }
        inventory.setItem(53, BACK_ITEM)
    }
}

class EnabledUser(private val voteFile: VoteFile)
{
    fun getSkull(): ItemStack
    {
        val skull = Bukkit.getOfflinePlayer(voteFile.name).getSkull()
        val meta = skull.itemMeta
        meta!!.lore = listOf(
            ChatColor.GRAY.toString() + "Enabled: " + if (voteFile.isOpUser)
                ChatColor.GREEN.toString() + "Yes" else ChatColor.RED.toString() + "No",
            ChatColor.GRAY.toString() + "This setting overrides the group permissions."
        )
        if (meta.displayName != voteFile.name)
        {
            meta.setDisplayName(ChatColor.AQUA.toString() + voteFile.name)
        }
        skull.itemMeta = meta
        return skull
    }
}
