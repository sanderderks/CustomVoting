package me.sd_master92.customvoting.database

import me.sd_master92.core.database.CustomColumn
import me.sd_master92.core.database.CustomTable
import me.sd_master92.core.errorLog
import me.sd_master92.core.infoLog
import me.sd_master92.customvoting.CV

enum class PlayerTableColumn(
    val columnName: String,
    val dataType: CustomColumn.DataType,
    var defaultValue: Any?
)
{
    UUID("uuid", CustomColumn.DataType.VARCHAR_PRIMARY, null),
    NAME("name", CustomColumn.DataType.VARCHAR, "Unknown"),
    VOTES("votes", CustomColumn.DataType.INT, 0),
    MONTHLY_VOTES("votes_monthly", CustomColumn.DataType.INT, 0),
    WEEKLY_VOTES("votes_weekly", CustomColumn.DataType.INT, 0),
    DAILY_VOTES("votes_daily", CustomColumn.DataType.INT, 0),
    DAILY_VOTE_STREAK("streak_daily", CustomColumn.DataType.INT, 0),
    LAST_VOTE("last", CustomColumn.DataType.LONG, 0),
    LAST_SITE("last_site", CustomColumn.DataType.VARCHAR, ""),
    POWER("power", CustomColumn.DataType.BOOLEAN, 0);

    companion object
    {
        fun create(plugin: CV, table: CustomTable)
        {
            if (!table.create(UUID.columnName, UUID.dataType))
            {
                plugin.errorLog("| could not create table '$table'")
                plugin.errorLog("|")
            } else
            {
                for (column in columns())
                {
                    if (!table.getColumn(column.columnName).create(column.dataType))
                    {
                        plugin.errorLog("| could not create column '${column.columnName}'")
                    }
                }
                plugin.infoLog("| successfully created table '${table.name}'")
                plugin.infoLog("|")
            }
        }

        fun columns(): Array<PlayerTableColumn>
        {
            return values().filter { it != UUID }.toTypedArray()
        }

        fun columns(uuid: String): Array<PlayerTableColumn>
        {
            return values().map {
                if (it == UUID)
                {
                    it.defaultValue = uuid
                }
                it
            }.toTypedArray()
        }
    }
}