package me.sd_master92.customvoting.gui.rewards

import me.sd_master92.core.appendWhenTrue
import me.sd_master92.core.input.PlayerNumberInput
import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.*
import me.sd_master92.customvoting.gui.VoteSettings
import me.sd_master92.customvoting.gui.items.CommandsRewardItem
import me.sd_master92.customvoting.gui.items.ItemsRewardItem
import me.sd_master92.customvoting.gui.rewards.crate.Crates
import me.sd_master92.customvoting.gui.rewards.milestones.Milestones
import me.sd_master92.customvoting.listeners.PlayerCommandInput
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class RewardSettings(private val plugin: CV, private val op: Boolean = false) :
    GUI(
        plugin,
        if (!op) Strings.VOTE_REWARDS_INVENTORY_NAME.toString() else Strings.PERMISSION_BASED_REWARDS_INVENTORY_NAME.toString(),
        if (op) 9 else 18
    )
{
    override fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)
    {
        when (item.type)
        {
            Material.BARRIER           ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                if (op)
                {
                    player.openInventory(RewardSettings(plugin).inventory)
                } else
                {
                    player.openInventory(VoteSettings(plugin).inventory)
                }
            }

            Material.CHEST             ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                player.openInventory(ItemRewards(plugin, op).inventory)
            }

            Material.REPEATER          ->
            {
                SoundType.CHANGE.play(plugin, player)
                val path = Setting.ITEM_REWARD_TYPE.path.appendWhenTrue(op, Data.OP_REWARDS)
                plugin.config.setNumber(path, ItemRewardType.next(plugin, op).value)
                event.currentItem = ItemsRewardTypeItem(plugin, op)
            }

            Material.GOLD_INGOT        -> if (CV.ECONOMY != null)
            {
                SoundType.CHANGE.play(plugin, player)
                cancelCloseEvent = true
                player.closeInventory()
                player.sendMessage(Strings.GENERAL_MESSAGE_NUMBER_ENTER.toString())
                player.sendMessage(Strings.GENERAL_MESSAGE_CANCEL_BACK.toString())
                object : PlayerNumberInput(plugin, player)
                {
                    override fun onNumberReceived(input: Int)
                    {
                        SoundType.SUCCESS.play(plugin, player)
                        val path = Setting.VOTE_REWARD_MONEY.path.appendWhenTrue(op, Data.OP_REWARDS)
                        plugin.config[path] = input
                        plugin.config.saveConfig()
                        player.sendMessage(Strings.GENERAL_MESSAGE_UPDATE_SUCCESS_X.with(Strings.MONEY_REWARD_UNIT.toString()))
                        player.openInventory(RewardSettings(plugin, op).inventory)
                        cancel()
                    }

                    override fun onCancel()
                    {
                        SoundType.FAILURE.play(plugin, player)
                        player.openInventory(RewardSettings(plugin, op).inventory)
                    }
                }
            } else
            {
                SoundType.FAILURE.play(plugin, player)
            }

            Material.EXPERIENCE_BOTTLE ->
            {
                SoundType.CHANGE.play(plugin, player)
                val path = Setting.VOTE_REWARD_EXPERIENCE.path.appendWhenTrue(op, Data.OP_REWARDS)
                if (plugin.config.getNumber(path) < 10)
                {
                    plugin.config.addNumber(path, 1)
                } else
                {
                    plugin.config.setNumber(path, 0)
                }
                event.currentItem = ExperienceRewardItem(plugin, op)
            }

            Material.COMMAND_BLOCK     ->
            {
                SoundType.CHANGE.play(plugin, player)
                cancelCloseEvent = true
                player.closeInventory()
                object : PlayerCommandInput(plugin, player, Data.VOTE_COMMANDS.path.appendWhenTrue(op, Data.OP_REWARDS))
                {
                    override fun onCommandReceived()
                    {
                        SoundType.SUCCESS.play(plugin, player)
                        player.openInventory(RewardSettings(plugin, op).inventory)
                    }

                    override fun onCancel()
                    {
                        SoundType.FAILURE.play(plugin, player)
                        player.openInventory(RewardSettings(plugin, op).inventory)
                    }
                }
            }

            Material.ENDER_CHEST       ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                player.openInventory(LuckyRewards(plugin).inventory)
            }

            Material.ENDER_EYE         ->
            {
                SoundType.CHANGE.play(plugin, player)
                val chance: Int = plugin.config.getNumber(Setting.LUCKY_VOTE_CHANCE.path)
                if (chance < 10)
                {
                    plugin.config.addNumber(Setting.LUCKY_VOTE_CHANCE.path, 1)
                } else if (chance < 100)
                {
                    plugin.config.addNumber(Setting.LUCKY_VOTE_CHANCE.path, 5)
                } else
                {
                    plugin.config.setNumber(Setting.LUCKY_VOTE_CHANCE.path, 1)
                }
                event.currentItem = LuckyVoteChanceItem(plugin)
            }

            Material.NETHER_STAR       ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                player.openInventory(Milestones(plugin).inventory)
            }

            Material.DIAMOND_BLOCK     ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                player.openInventory(RewardSettings(plugin, true).inventory)
            }

            Material.TNT               ->
            {
                SoundType.CHANGE.play(plugin, player)
                cancelCloseEvent = true
                player.closeInventory()
                object : PlayerCommandInput(plugin, player, Data.VOTE_PARTY_COMMANDS.path)
                {
                    override fun onCommandReceived()
                    {
                        SoundType.SUCCESS.play(plugin, player)
                        player.openInventory(RewardSettings(plugin, op).inventory)
                    }

                    override fun onCancel()
                    {
                        SoundType.FAILURE.play(plugin, player)
                        player.openInventory(RewardSettings(plugin, op).inventory)
                    }
                }
            }

            Material.BELL              ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                player.openInventory(EnabledGroups(plugin).inventory)
            }

            Material.PLAYER_HEAD       ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                player.openInventory(EnabledUsers(plugin).inventory)
            }

            Material.TRIPWIRE_HOOK     ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                player.openInventory(Crates(plugin).inventory)
            }

            else                       ->
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
        inventory.addItem(ItemsRewardItem(plugin, Data.ITEM_REWARDS.path.appendWhenTrue(op, Data.OP_REWARDS)))
        inventory.addItem(ItemsRewardTypeItem(plugin, op))
        inventory.addItem(MoneyRewardItem(plugin, op))
        inventory.addItem(ExperienceRewardItem(plugin, op))
        inventory.addItem(
            CommandsRewardItem(
                plugin,
                Data.VOTE_COMMANDS.path.appendWhenTrue(op, Data.OP_REWARDS),
                Material.COMMAND_BLOCK
            )
        )
        if (!op)
        {
            inventory.addItem(
                BaseItem(
                    Material.ENDER_CHEST, Strings.LUCKY_VOTE_ITEM_NAME_REWARDS.toString(),
                    Strings.GENERAL_ITEM_LORE_CURRENT_XY.with(
                        "" + plugin.data.getItems(Data.LUCKY_REWARDS.path).size,
                        Strings.ITEM_REWARDS_UNIT_STACKS_MULTIPLE.toString()
                    )
                )
            )
            inventory.addItem(LuckyVoteChanceItem(plugin))
            inventory.addItem(BaseItem(Material.NETHER_STAR, Strings.MILESTONE_ITEM_NAME_OVERVIEW.toString()))
            inventory.addItem(
                BaseItem(
                    Material.DIAMOND_BLOCK,
                    Strings.PERMISSION_BASED_REWARDS_ITEM_NAME.toString(),
                    null,
                    true
                )
            )
            inventory.addItem(
                CommandsRewardItem(
                    plugin,
                    Data.VOTE_PARTY_COMMANDS.path,
                    Material.TNT,
                    Strings.COMMAND_REWARDS_ITEM_NAME_VOTE_PARTY.toString()
                )
            )
        }
        if (op)
        {
            if (CV.PERMISSION != null)
            {
                inventory.addItem(
                    BaseItem(
                        Material.BELL,
                        Strings.ENABLED_GROUP_OVERVIEW_ITEM_NAME.toString()
                    )
                )
            }
            inventory.addItem(
                BaseItem(
                    Material.PLAYER_HEAD,
                    Strings.ENABLED_USER_OVERVIEW_ITEM_NAME.toString()
                )
            )
        }
        inventory.addItem(
            BaseItem(
                Material.TRIPWIRE_HOOK,
                Strings.CRATE_OVERVIEW_ITEM_NAME.toString(),
                null,
                true
            )
        )
        inventory.setItem(if (op) 8 else 17, BACK_ITEM)
    }
}

class MoneyRewardItem(plugin: CV, op: Boolean) : BaseItem(
    Material.GOLD_INGOT, Strings.MONEY_REWARD_ITEM_NAME.toString(),
    if (CV.ECONOMY != null) Strings.GENERAL_ITEM_LORE_CURRENT_X.with(
        ChatColor.GREEN.toString() +
                CV.ECONOMY!!.format(
                    plugin.config.getDouble(Setting.VOTE_REWARD_MONEY.path.appendWhenTrue(op, Data.OP_REWARDS))
                )
    ) else Strings.GENERAL_VALUE_DISABLED.toString()
)

class LuckyVoteChanceItem(plugin: CV) : BaseItem(
    Material.ENDER_EYE, Strings.LUCKY_VOTE_ITEM_NAME_CHANCE.toString(),
    Strings.GENERAL_ITEM_LORE_CURRENT_XY.with("" + plugin.config.getNumber(Setting.LUCKY_VOTE_CHANCE.path), "%")
)

class ItemsRewardTypeItem(plugin: CV, op: Boolean) : BaseItem(
    Material.REPEATER, Strings.ITEM_REWARDS_ITEM_NAME_TYPE.toString(),
    Strings.GENERAL_ITEM_LORE_STATUS_X.with(
        ItemRewardType.valueOf(
            plugin.config.getNumber(Setting.ITEM_REWARD_TYPE.path.appendWhenTrue(op, Data.OP_REWARDS))
        ).label
    )
)

class ExperienceRewardItem(plugin: CV, op: Boolean) : BaseItem(
    Material.EXPERIENCE_BOTTLE, Strings.XP_REWARD_ITEM_NAME.toString(),
    Strings.GENERAL_ITEM_LORE_CURRENT_XY.with(
        "" + plugin.config.getNumber(
            Setting.VOTE_REWARD_EXPERIENCE.path.appendWhenTrue(
                op,
                Data.OP_REWARDS
            )
        ), Strings.XP_UNIT_LEVELS_MULTIPLE.toString()
    )
)