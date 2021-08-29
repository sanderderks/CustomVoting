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

class Main : CustomPlugin(28103, "settings.yml")
{
    lateinit var playerTable: PlayerTable
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
            playerTable.table.database.disconnect()
        }
    }

    private fun checkVotifier(): Boolean
    {
        print("")
        print("| checking for Votifier")
        print("|")
        return try
        {
            Class.forName("com.vexsoftware.votifier.model.VotifierEvent")
            print("|___dependency 'Votifier' found!")
            true
        } catch (e: ClassNotFoundException)
        {
            error("|___dependency 'Votifier' not found, disabling...")
            isEnabled = false
            false
        }
    }

    private fun checkHooks()
    {
        print("")
        print("| checking for economy hook")
        print("|")
        if (!setupEconomy())
        {
            error("|___economy hook not found")
        } else
        {
            print("|___successfully hooked into '" + economy!!.name + "'")
        }
        print("")
        print("| checking for PlaceholderAPI hook")
        print("|")
        if (server.pluginManager.getPlugin("PlaceholderAPI") == null)
        {
            error("|___PlaceholderAPI hook not found")
        } else
        {
            CustomPlaceholders(this).register()
            print("|___successfully hooked into PlaceholderAPI")
        }
    }

    private fun setupEconomy(): Boolean
    {
        if (server.pluginManager.getPlugin("Vault") == null)
        {
            return false
        }
        val rsp = server.servicesManager.getRegistration(Economy::class.java) ?: return false
        economy = rsp.provider
        return true
    }

    private fun registerFiles()
    {
        messages = CustomFile("messages.yml", this)
        data = CustomFile("data.yml", this)
        Settings.initialize(this)
    }

    private fun setupDatabase()
    {
        print("")
        print("| connecting to database")
        print("|")
        if (useDatabase())
        {
            val database = CustomDatabase(config, Settings.DATABASE)
            if (!database.connect())
            {
                error("|___could not connect to database")
            } else
            {
                playerTable = PlayerTable(this, database)
            }
        } else
        {
            print("|___database is disabled in the config")
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
            playerTable.table.database.isConnected
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
        CreateTopCommand(this)
        DeleteTopCommand(this)
        FakeVoteCommand(this)
        ReloadCommand(this)
        SettingsCommand(this)
        SetVotesCommand(this)
        VoteCommand(this)
        VotePartyCommand(this)
        VotesCommand(this)
        VoteTopCommand(this)
    }

    private fun startTasks()
    {
        DailyTask(this)
    }

    companion object
    {
        var economy: Economy? = null
    }
}