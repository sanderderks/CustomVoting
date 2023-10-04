package me.sd_master92.customvoting.gui.items

import me.sd_master92.customvoting.constants.enumerations.PMessage
import org.bukkit.Material

class VotePartyItem(key: String) : SimpleItem(
    Material.ENDER_CHEST,
    PMessage.VOTE_PARTY_ITEM_NAME_CHEST_X.with(key),
    PMessage.VOTE_PARTY_ITEM_LORE_CHEST.toString()
)