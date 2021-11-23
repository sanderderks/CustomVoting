package me.sd_master92.customvoting

import me.sd_master92.customfile.CustomFile
import me.sd_master92.customfile.database.CustomDatabase
import me.sd_master92.customvoting.commands.*
import me.sd_master92.customvoting.commands.voteparty.VotePartyCommand
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

class Main : CustomPlugin("settings.yml", 28103)
{
    var playerTable: PlayerTable? = null
        private set
    lateinit var messages: CustomFile
        private set
    lateinit var data: CustomFile
        private set

    override fun enable()
    {
        if (!checkVotifier())
        {
            return
        }
        checkHooks()
        registerFiles()
        setupDatabase()
        registerListeners()
        registerCommands()
        startTasks()
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
            false
        }
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
                infoLog("|___successfully hooked into '" + economy!!.name + "'")
            }
            infoLog("")
            infoLog("| checking for permission hook")
            infoLog("|")
            if (!setupPermission())
            {
                errorLog("|___permission hook not found")
            } else
            {
                infoLog("|___successfully hooked into '" + permission!!.name + "'")
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
            CustomPlaceholders(this).register()
            infoLog("|___successfully hooked into PlaceholderAPI")
        }
    }

    private fun hasVault(): Boolean
    {
        return server.pluginManager.getPlugin("Vault") != null
    }

    private fun setupEconomy(): Boolean
    {
        economy = server.servicesManager.getRegistration(Economy::class.java)?.provider ?: return false
        return economy != null
    }

    private fun setupPermission(): Boolean
    {
        permission = server.servicesManager.getRegistration(Permission::class.java)?.provider ?: return false
        return permission != null
    }

    private fun registerFiles()
    {
        messages = CustomFile("messages.yml", this)
        data = CustomFile("data.yml", this)
        Settings.initialize(this)
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
        var economy: Economy? = null
        var permission: Permission? = null
    }
}