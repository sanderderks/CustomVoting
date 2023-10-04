package me.sd_master92.customvoting

import me.clip.placeholderapi.PlaceholderAPI
import me.sd_master92.core.file.PlayerFile
import me.sd_master92.core.plugin.CustomPlugin
import me.sd_master92.customvoting.constants.enumerations.Message
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.VoteSortType
import me.sd_master92.customvoting.constants.interfaces.Voter
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.command.CommandSender
import org.bukkit.entity.*
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.ceil


fun String.withPlaceholders(player: Player? = null): String
{
    return if (CV.PAPI)
    {
        PlaceholderAPI.setPlaceholders(player, this)
    } else
    {
        this
    }
}

fun String.replaceIfNotNull(oldValue: String, newValue: String?): String
{
    if (newValue != null)
    {
        return this.replace(oldValue, newValue)
    }
    return this
}

fun String.stripColor(): String
{
    return ChatColor.stripColor(this)!!
}

fun String.capitalize(): String
{
    return this.replaceFirstChar { it.uppercase() }
}

fun CommandSender?.sendText(message: String)
{
    if (this is Player)
    {
        this.sendMessage(message.withPlaceholders(this))
    } else
    {
        this?.sendMessage(message)
    }
}

fun CommandSender?.sendText(plugin: CV, message: Message, placeholders: Map<String, String> = HashMap())
{
    this?.sendText(message.getMessage(plugin, placeholders))
}

fun CommandSender?.sendTexts(messages: List<String>)
{
    if (this != null)
    {
        for (line in messages)
        {
            this.sendText(line)
        }
    }
}

fun CommandSender?.sendTexts(plugin: CV, message: Message, placeholders: Map<String, String> = HashMap())
{
    if (this != null)
    {
        for (line in message.getMessages(plugin, placeholders))
        {
            this.sendText(line)
        }
    }
}

fun Player?.sendActionBar(message: String)
{
    this?.spigot()?.sendMessage(
        ChatMessageType.ACTION_BAR,
        TextComponent(message.withPlaceholders(this))
    )
}

fun Player.addToInventoryOrDrop(items: Array<ItemStack>, random: Boolean = false)
{
    if (items.isNotEmpty())
    {
        if (random)
        {
            this.addToInventoryOrDrop(items[Random().nextInt(items.size)])
        } else
        {
            items.forEach { this.addToInventoryOrDrop(it) }
        }
    }
}

fun Player.addToInventoryOrDrop(item: ItemStack)
{
    this.inventory.addItem(item).values.forEach { this.world.dropItemNaturally(this.location, it) }
}

fun OfflinePlayer.hasPowerRewards(plugin: CV): Boolean
{
    return this.hasPowerRewardsByGroup(plugin) || this.hasPowerRewardsByUser(plugin)
}

fun OfflinePlayer.hasPowerRewardsByGroup(plugin: CV): Boolean
{
    if (CV.PERMISSION != null)
    {
        try
        {
            for (group in CV.PERMISSION!!.getPlayerGroups(null, this))
            {
                if (plugin.config.getStringList(Setting.ENABLED_PERM_GROUPS.path).contains(group.lowercase()))
                {
                    return true
                }
            }
        } catch (e: Exception)
        {
            return false
        }
    }
    return false
}

fun OfflinePlayer.hasPowerRewardsByUser(plugin: CV): Boolean
{
    if (this is Player)
    {
        return Voter.get(plugin, this).power
    }
    return false
}

fun Player.getPlayerFile(plugin: CV): PlayerFile
{
    return if (plugin.config.getBoolean(Setting.UUID_STORAGE.path)) PlayerFile.getByUuid(
        plugin,
        this
    ) else PlayerFile.getByName(plugin, this.name) ?: PlayerFile.getByUuid(plugin, this)
}

fun OfflinePlayer?.getSkull(): ItemStack
{
    val skull = ItemStack(Material.PLAYER_HEAD)
    if (this != null && this.hasPlayedBefore())
    {
        val skullMeta = skull.itemMeta as SkullMeta
        skullMeta.owningPlayer = this
        skullMeta.setDisplayName(
            PMessage.PLAYER_ITEM_NAME_SKULL_X.with(
                this.name ?: PMessage.PLAYER_NAME_UNKNOWN.toString()
            )
        )
        skull.itemMeta = skullMeta
    }
    return skull
}

fun String.getPlayerNameWithSuffix(plugin: CV): String
{
    val suffix = plugin.config.getString(Setting.SUFFIX_SUPPORT.path)
    if (suffix != null)
    {
        return "$suffix$this"
    }
    return this
}

fun String.getPlayer(plugin: CV): Player?
{
    return Bukkit.getPlayer(this) ?: Bukkit.getPlayer(this.getPlayerNameWithSuffix(plugin))
}

fun String.getOfflinePlayer(plugin: CV): OfflinePlayer?
{
    val offlinePlayers = Bukkit.getOfflinePlayers().toList()
    return offlinePlayers.firstOrNull { player -> player.name == this }
        ?: offlinePlayers.firstOrNull { player -> player.name == this.getPlayerNameWithSuffix(plugin) }
}

fun Location.spawnArmorStand(): ArmorStand
{
    val stand = world!!.spawnEntity(this, EntityType.ARMOR_STAND) as ArmorStand
    stand.removeWhenFarAway = false
    stand.isSilent = true
    stand.setGravity(false)
    stand.isCustomNameVisible = true
    stand.isInvulnerable = true
    stand.isVisible = false
    return stand
}

fun Long.dayDifferenceToday(): Int
{
    val calendar = Calendar.getInstance()
    calendar.time = Date(this)
    return Calendar.getInstance()[Calendar.DAY_OF_YEAR] - calendar[Calendar.DAY_OF_YEAR]
}

fun Long.weekDifferenceToday(): Int
{
    val calendar = Calendar.getInstance()
    calendar.time = Date(this)
    return Calendar.getInstance()[Calendar.WEEK_OF_YEAR] - calendar[Calendar.WEEK_OF_YEAR]
}

fun Long.monthDifferenceToday(): Int
{
    val calendar = Calendar.getInstance()
    calendar.time = Date(this)
    return Calendar.getInstance()[Calendar.MONTH] - calendar[Calendar.MONTH]
}

fun Long.toTimeString(): String
{
    return if (this <= 0L)
    {
        "${PMessage.GREEN}00${PMessage.GRAY}h ${PMessage.GREEN}00${PMessage.GRAY}m ${PMessage.GREEN}00${PMessage.GRAY}s"
    } else
    {
        val hours = String.format("%02d", TimeUnit.MILLISECONDS.toHours(this))
        val minutes = String.format("%02d", TimeUnit.MILLISECONDS.toMinutes(this) % 60)
        val seconds = String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(this) % 60)
        "${PMessage.RED}$hours${PMessage.GRAY}h ${PMessage.RED}$minutes${PMessage.GRAY}m ${PMessage.RED}$seconds${PMessage.GRAY}s"
    }
}

fun Locale.setLanguage()
{
    CV.RESOURCE_BUNDLE = ResourceBundle.getBundle("language/messages", this)

    CustomPlugin.SAVE_TEXT = PMessage.GENERAL_VALUE_SAVE.toString()
    CustomPlugin.BACK_TEXT = PMessage.GENERAL_VALUE_BACK.toString()
    CustomPlugin.STATUS_TEXT = PMessage.GENERAL_ITEM_LORE_STATUS.toString()
    CustomPlugin.ON_TEXT = PMessage.GENERAL_VALUE_ON.toString()
    CustomPlugin.OFF_TEXT = PMessage.GENERAL_VALUE_OFF.toString()
}

fun LivingEntity.getEntityHealthString(): String
{
    val redHeart = "${ChatColor.DARK_RED}❤"
    val halfRedHeart = "${ChatColor.RED}❤"
    val grayHeart = "${ChatColor.GRAY}❤"
    val maxHealth = getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value ?: 20.0

    val health = ceil(health / (maxHealth / 20)).toInt()

    return buildString {
        for (i in 0 until 10)
        {
            append(
                when
                {
                    i * 2 + 2 <= health -> redHeart
                    i * 2 + 1 == health -> halfRedHeart
                    else                -> grayHeart
                }
            )
        }
    }
}

fun Location.splashPotion(mat: Material, type: PotionEffectType)
{
    val potion = ItemStack(mat)
        .apply {
            val potionMeta = itemMeta as PotionMeta
            potionMeta.addCustomEffect(PotionEffect(type, 0, 0), true)
            itemMeta = potionMeta
        }

    val thrownPotion = world!!.spawn(this, ThrownPotion::class.java)
    thrownPotion.item = potion
    thrownPotion.shooter = null
    thrownPotion.velocity = direction.multiply(1.5)
}

fun String.trimPrefixColor(): String
{
    return replaceFirst(Regex("^§."), "")
}

fun Voter.getVotesPlaceholders(plugin: CV): MutableMap<String, String>
{
    val placeholders: MutableMap<String, String> = HashMap()
    placeholders["%PLAYER%"] = name
    placeholders["%VOTES%"] = "$votes"
    placeholders["%VOTES_MONTHLY%"] = "$votesMonthly"
    placeholders["%VOTES_WEEKLY%"] = "$votesWeekly"
    placeholders["%VOTES_DAILY%"] = "$votesDaily"

    when (VoteSortType.valueOf(plugin.config.getNumber(Setting.VOTES_SORT_TYPE.path)))
    {
        VoteSortType.ALL     ->
        {
            placeholders["%VOTES_AUTO%"] = "$votes"
            placeholders["%s%"] = if (votes == 1) "" else "s"
        }

        VoteSortType.MONTHLY ->
        {
            placeholders["%VOTES_AUTO%"] = "$votesMonthly"
            placeholders["%s%"] = if (votesMonthly == 1) "" else "s"
        }

        VoteSortType.WEEKLY  ->
        {
            placeholders["%VOTES_AUTO%"] = "$votesWeekly"
            placeholders["%s%"] = if (votesWeekly == 1) "" else "s"
        }

        VoteSortType.DAILY   ->
        {
            placeholders["%VOTES_AUTO%"] = "$votesDaily"
            placeholders["%s%"] = if (votesDaily == 1) "" else "s"
        }
    }
    return placeholders
}