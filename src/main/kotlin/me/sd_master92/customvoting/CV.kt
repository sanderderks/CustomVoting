package me.sd_master92.customvoting

import me.sd_master92.customfile.CustomFile
import me.sd_master92.customfile.PlayerFile
import me.sd_master92.customfile.database.CustomDatabase
import me.sd_master92.customvoting.commands.*
import me.sd_master92.customvoting.commands.voteparty.VotePartyCommand
import me.sd_master92.customvoting.constants.Messages
import me.sd_master92.customvoting.constants.Settings
import me.sd_master92.customvoting.database.PlayerTable
import me.sd_master92.customvoting.extensions.CustomPlaceholders
import me.sd_master92.customvoting.listeners.PlayerListener
import me.sd_master92.customvoting.listeners.VoteTopListener
import me.sd_master92.customvoting.listeners.VotifierListener
import me.sd_master92.customvoting.tasks.DailyTask
import me.sd_master92.plugin.CustomPlugin
import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.permission.Permission
import org.bstats.bukkit.Metrics
import org.bstats.charts.SimplePie
import org.bukkit.Bukkit


class CV : CustomPlugin(
    "settings.yml",
    28103
)
{
    var playerTable: PlayerTable? = null
        private set
    lateinit var messages: CustomFile
        private set
    lateinit var data: CustomFile
        private set

    override fun enable()
    {
        if (!checkVotifier() || !checkMCVersion())
        {
            return
        }
        checkHooks()
        registerFiles()
        setupDatabase()
        registerListeners()
        registerCommands()
        startTasks()
        setupBStatsMetrics()
    }

    override fun disable()
    {
        if (hasDatabaseConnection())
        {
            playerTable!!.table.database.disconnect()
        }
    }

    private fun checkVotifier(): Boolean
    {
        infoLog("")
        infoLog("| checking for Votifier")
        infoLog("|")
        return try
        {
            Class.forName("com.vexsoftware.votifier.model.VotifierEvent")
            infoLog("|___dependency 'Votifier' found!")
            true
        } catch (e: ClassNotFoundException)
        {
            errorLog("|___dependency 'Votifier' not found, disabling...")
            isEnabled = false
            server.pluginManager.disablePlugin(this)
            false
        }
    }

    private fun checkMCVersion(): Boolean
    {
        infoLog("")
        infoLog("| checking Minecraft version")
        infoLog("|")
        MC_VERSION = with(Bukkit.getBukkitVersion().split("-")[0])
        {
            when
            {
                contains("1.18") -> 18
                contains("1.17") -> 17
                contains("1.16") -> 16
                contains("1.15") -> 15
                contains("1.14") -> 14
                else             -> 0
            }
        }
        if (MC_VERSION == 0)
        {
            errorLog("|___detected invalid Minecraft version, disabling...")
            isEnabled = false
            server.pluginManager.disablePlugin(this)
            return false
        } else
        {
            infoLog("|___up to date!")
        }
        return true
    }

    private fun checkHooks()
    {
        infoLog("")
        infoLog("| checking for Vault")
        infoLog("|")
        if (hasVault())
        {
            infoLog("|___Vault found")
            infoLog("")
            infoLog("| checking for economy hook")
            infoLog("|")
            if (!setupEconomy())
            {
                errorLog("|___economy hook not found")
            } else
            {
                infoLog("|___successfully hooked into '" + ECONOMY!!.name + "'")
            }
            infoLog("")
            infoLog("| checking for permission hook")
            infoLog("|")
            if (!setupPermission())
            {
                errorLog("|___permission hook not found")
            } else
            {
                infoLog("|___successfully hooked into '" + PERMISSION!!.name + "'")
            }
        } else
        {
            errorLog("| Vault not found")
            errorLog("|")
            errorLog("|___Economy and permissions disabled")
        }
        infoLog("")
        infoLog("| checking for PlaceholderAPI hook")
        infoLog("|")
        if (server.pluginManager.getPlugin("PlaceholderAPI") == null)
        {
            errorLog("|___PlaceholderAPI hook not found")
        } else
        {
            PAPI = true
            CustomPlaceholders(this).register()
            infoLog("|___successfully hooked into PlaceholderAPI")
        }
        infoLog("")
        infoLog("| checking for Citizens hook")
        infoLog("|")
        if (server.pluginManager.getPlugin("Citizens") == null)
        {
            errorLog("|___Citizens hook not found")
        } else
        {
            CITIZENS = true
            infoLog("|___successfully hooked into Citizens")
        }
    }

    private fun hasVault(): Boolean
    {
        return server.pluginManager.getPlugin("Vault") != null
    }

    private fun setupEconomy(): Boolean
    {
        ECONOMY = server.servicesManager.getRegistration(Economy::class.java)?.provider ?: return false
        return ECONOMY != null
    }

    private fun setupPermission(): Boolean
    {
        PERMISSION = server.servicesManager.getRegistration(Permission::class.java)?.provider ?: return false
        return PERMISSION != null
    }

    private fun registerFiles()
    {
        messages = CustomFile("messages.yml", this)
        data = CustomFile("data.yml", this)
        PlayerFile.init(this)
        Settings.initialize(this)
        Messages.initialize(this)
    }

    private fun setupDatabase()
    {
        infoLog("")
        infoLog("| connecting to database")
        infoLog("|")
        if (useDatabase())
        {
            val database = CustomDatabase(config, Settings.DATABASE)
            if (!database.connect())
            {
                errorLog("|___could not connect to database")
            } else
            {
                playerTable = PlayerTable(this, database)
            }
        } else
        {
            errorLog("|___database is disabled in the config")
        }
    }

    private fun setupBStatsMetrics()
    {
        val metrics = Metrics(this, 13544)
        metrics.addCustomChart(SimplePie("ingame_updates_enabled") { if (config.getBoolean(Settings.INGAME_UPDATES)) "true" else "false" })
        metrics.addCustomChart(SimplePie("database_enabled") { if (useDatabase()) "true" else "false" })
        metrics.addCustomChart(SimplePie("vote_party_enabled") { if (config.getBoolean(Settings.VOTE_PARTY)) "true" else "false" })
        metrics.addCustomChart(SimplePie("lucky_vote_enabled") { if (config.getBoolean(Settings.LUCKY_VOTE)) "true" else "false" })
    }

    private fun useDatabase(): Boolean
    {
        return config.getBoolean(Settings.USE_DATABASE)
    }

    fun hasDatabaseConnection(): Boolean
    {
        return if (useDatabase())
        {
            playerTable?.table?.database?.isConnected ?: false
        } else
        {
            false
        }
    }

    private fun registerListeners()
    {
        registerListener(PlayerListener(this))
        registerListener(VotifierListener(this))
        registerListener(VoteTopListener(this))
    }

    private fun registerCommands()
    {
        CreateTopCommand(this).register()
        DeleteTopCommand(this).register()
        FakeVoteCommand(this).register()
        ReloadCommand(this).register()
        SettingsCommand(this).register()
        SetVotesCommand(this).register()
        ClearPeriodVotesCommand(this).register()
        VoteCommand(this).register()
        VotePartyCommand(this).register()
        VotesCommand(this).register()
        VoteTopCommand(this).register()
    }

    private fun startTasks()
    {
        DailyTask(this)
    }

    companion object
    {
        var PAPI = false
            private set
        var CITIZENS = false
            private set
        var ECONOMY: Economy? = null
            private set
        var PERMISSION: Permission? = null
            private set
        var MC_VERSION: Int = 0
            private set
    }
}