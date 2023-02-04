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
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class RewardSettings(private val plugin: CV, private val op: Boolean = false) :
    GUI(
        plugin,
        if (!op) PMessage.VOTE_REWARDS_INVENTORY_NAME.toString() else PMessage.PERMISSION_BASED_REWARDS_INVENTORY_NAME.toString(),
        if (op) 9 else 18
    )
{
    override fun onBack(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        cancelCloseEvent = true
        if (op)
        {
            RewardSettings(plugin).open(player)
        } else
        {
            VoteSettings(plugin).open(player)
        }
    }

    override fun onClick(event: InventoryClickEvent, player: Player)
    {
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        SoundType.CLOSE.play(plugin, player)
    }

    override fun onSave(event: InventoryClickEvent, player: Player)
    {
    }

    init
    {
        addItem(object : ItemsRewardItem(plugin, Data.ITEM_REWARDS.path.appendWhenTrue(op, Data.OP_REWARDS))
        {
            override fun onClick(event: InventoryClickEvent, player: Player)
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                ItemRewards(plugin, op).open(player)
            }
        })
        addItem(ItemsRewardTypeItem(plugin, op))
        addItem(object : BaseItem(
            Material.GOLD_INGOT, PMessage.MONEY_REWARD_ITEM_NAME.toString(),
            if (CV.ECONOMY != null) PMessage.GENERAL_ITEM_LORE_CURRENT_X.with(
                PMessage.GREEN.toString() +
                        CV.ECONOMY!!.format(
                            plugin.config.getDouble(Setting.VOTE_REWARD_MONEY.path.appendWhenTrue(op, Data.OP_REWARDS))
                        )
            ) else PMessage.GENERAL_VALUE_DISABLED.toString()
        )
        {
            override fun onClick(event: InventoryClickEvent, player: Player)
            {
                if (CV.ECONOMY != null)
                {
                    SoundType.CHANGE.play(plugin, player)
                    cancelCloseEvent = true
                    player.closeInventory()
                    player.sendMessage(PMessage.GENERAL_MESSAGE_NUMBER_ENTER.toString())
                    player.sendMessage(PMessage.GENERAL_MESSAGE_CANCEL_BACK.toString())
                    object : PlayerNumberInput(plugin, player)
                    {
                        override fun onNumberReceived(input: Int)
                        {
                            SoundType.SUCCESS.play(plugin, player)
                            val path = Setting.VOTE_REWARD_MONEY.path.appendWhenTrue(op, Data.OP_REWARDS)
                            plugin.config[path] = input
                            plugin.config.saveConfig()
                            player.sendMessage(PMessage.GENERAL_MESSAGE_UPDATE_SUCCESS_X.with(PMessage.MONEY_REWARD_UNIT.toString()))
                            RewardSettings(plugin, op).open(player)
                            cancel()
                        }

                        override fun onCancel()
                        {
                            SoundType.FAILURE.play(plugin, player)
                            RewardSettings(plugin, op).open(player)
                        }
                    }
                } else
                {
                    SoundType.FAILURE.play(plugin, player)
                }
            }
        })
        addItem(ExperienceRewardItem(plugin, op))
        addItem(
            object : CommandsRewardItem(
                plugin,
                Data.VOTE_COMMANDS.path.appendWhenTrue(op, Data.OP_REWARDS),
                Material.COMMAND_BLOCK
            )
            {
                override fun onClick(event: InventoryClickEvent, player: Player)
                {
                    SoundType.CHANGE.play(plugin, player)
                    cancelCloseEvent = true
                    player.closeInventory()
                    object :
                        PlayerCommandInput(plugin, player, Data.VOTE_COMMANDS.path.appendWhenTrue(op, Data.OP_REWARDS))
                    {
                        override fun onCommandReceived()
                        {
                            SoundType.SUCCESS.play(plugin, player)
                            RewardSettings(plugin, op).open(player)
                        }

                        override fun onCancel()
                        {
                            SoundType.FAILURE.play(plugin, player)
                            RewardSettings(plugin, op).open(player)
                        }
                    }
                }
            }
        )
        if (!op)
        {
            addItem(
                object : BaseItem(
                    Material.ENDER_CHEST, PMessage.LUCKY_VOTE_ITEM_NAME_REWARDS.toString(),
                    PMessage.GENERAL_ITEM_LORE_CURRENT_XY.with(
                        "" + plugin.data.getItems(Data.LUCKY_REWARDS.path).size,
                        PMessage.ITEM_REWARDS_UNIT_STACKS_MULTIPLE.toString()
                    )
                )
                {
                    override fun onClick(event: InventoryClickEvent, player: Player)
                    {
                        SoundType.CLICK.play(plugin, player)
                        cancelCloseEvent = true
                        LuckyRewards(plugin).open(player)
                    }
                }
            )
            addItem(LuckyVoteChanceItem(plugin))
            addItem(object : BaseItem(Material.NETHER_STAR, PMessage.MILESTONE_ITEM_NAME_OVERVIEW.toString())
            {
                override fun onClick(event: InventoryClickEvent, player: Player)
                {
                    SoundType.CLICK.play(plugin, player)
                    cancelCloseEvent = true
                    Milestones(plugin).open(player)
                }
            })
            addItem(
                object : BaseItem(
                    Material.DIAMOND_BLOCK,
                    PMessage.PERMISSION_BASED_REWARDS_ITEM_NAME.toString(),
                    null,
                    true
                )
                {
                    override fun onClick(event: InventoryClickEvent, player: Player)
                    {
                        SoundType.CLICK.play(plugin, player)
                        cancelCloseEvent = true
                        RewardSettings(plugin, true).open(player)
                    }
                }
            )
            addItem(
                object : CommandsRewardItem(
                    plugin,
                    Data.VOTE_PARTY_COMMANDS.path,
                    Material.TNT,
                    PMessage.COMMAND_REWARDS_ITEM_NAME_VOTE_PARTY.toString()
                )
                {
                    override fun onClick(event: InventoryClickEvent, player: Player)
                    {
                        SoundType.CHANGE.play(plugin, player)
                        cancelCloseEvent = true
                        player.closeInventory()
                        object : PlayerCommandInput(plugin, player, Data.VOTE_PARTY_COMMANDS.path)
                        {
                            override fun onCommandReceived()
                            {
                                SoundType.SUCCESS.play(plugin, player)
                                RewardSettings(plugin, op).open(player)
                            }

                            override fun onCancel()
                            {
                                SoundType.FAILURE.play(plugin, player)
                                RewardSettings(plugin, op).open(player)
                            }
                        }
                    }
                }
            )
        }
        if (op)
        {
            if (CV.PERMISSION != null)
            {
                addItem(
                    object : BaseItem(
                        Material.BELL,
                        PMessage.ENABLED_GROUP_OVERVIEW_ITEM_NAME.toString()
                    )
                    {
                        override fun onClick(event: InventoryClickEvent, player: Player)
                        {
                            SoundType.CLICK.play(plugin, player)
                            cancelCloseEvent = true
                            EnabledGroups(plugin).open(player)
                        }
                    }
                )
            }
            addItem(
                object : BaseItem(
                    Material.PLAYER_HEAD,
                    PMessage.ENABLED_USER_OVERVIEW_ITEM_NAME.toString()
                )
                {
                    override fun onClick(event: InventoryClickEvent, player: Player)
                    {
                        SoundType.CLICK.play(plugin, player)
                        cancelCloseEvent = true
                        EnabledUsers(plugin).open(player)
                    }
                }
            )
        }
        addItem(
            object : BaseItem(
                Material.TRIPWIRE_HOOK,
                PMessage.CRATE_OVERVIEW_ITEM_NAME.toString(),
                null,
                true
            )
            {
                override fun onClick(event: InventoryClickEvent, player: Player)
                {
                    SoundType.CLICK.play(plugin, player)
                    cancelCloseEvent = true
                    Crates(plugin).open(player)
                }
            }
        )
    }
}

class LuckyVoteChanceItem(private val plugin: CV) : BaseItem(
    Material.ENDER_EYE, PMessage.LUCKY_VOTE_ITEM_NAME_CHANCE.toString(),
    PMessage.GENERAL_ITEM_LORE_CURRENT_XY.with("" + plugin.config.getNumber(Setting.LUCKY_VOTE_CHANCE.path), "%")
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
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
}

class ItemsRewardTypeItem(private val plugin: CV, private val op: Boolean) : BaseItem(
    Material.REPEATER, PMessage.ITEM_REWARDS_ITEM_NAME_TYPE.toString(),
    PMessage.GENERAL_ITEM_LORE_STATUS_X.with(
        ItemRewardType.valueOf(
            plugin.config.getNumber(Setting.ITEM_REWARD_TYPE.path.appendWhenTrue(op, Data.OP_REWARDS))
        ).label
    )
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        val path = Setting.ITEM_REWARD_TYPE.path.appendWhenTrue(op, Data.OP_REWARDS)
        plugin.config.setNumber(path, ItemRewardType.next(plugin, op).value)
        event.currentItem = ItemsRewardTypeItem(plugin, op)
    }
}

class ExperienceRewardItem(private val plugin: CV, private val op: Boolean) : BaseItem(
    Material.EXPERIENCE_BOTTLE, PMessage.XP_REWARD_ITEM_NAME.toString(),
    PMessage.GENERAL_ITEM_LORE_CURRENT_XY.with(
        "" + plugin.config.getNumber(
            Setting.VOTE_REWARD_EXPERIENCE.path.appendWhenTrue(
                op,
                Data.OP_REWARDS
            )
        ), PMessage.XP_UNIT_LEVELS_MULTIPLE.toString()
    )
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
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
}