package me.sd_master92.customvoting.database

import me.sd_master92.core.database.CustomColumn
import me.sd_master92.core.database.CustomTable
import me.sd_master92.core.errorLog
import me.sd_master92.core.infoLog
import me.sd_master92.customvoting.CV

enum class QueueTableColumn(
    val columnName: String,
    val dataType: CustomColumn.DataType
)
{
    ID("id", CustomColumn.DataType.INT_PRIMARY),
    UUID("uuid", CustomColumn.DataType.VARCHAR),
    SITE("votes", CustomColumn.DataType.VARCHAR);

    companion object
    {
        fun create(plugin: CV, table: CustomTable)
        {
            if (!table.create(ID.columnName, ID.dataType))
            {
                plugin.errorLog("| could not create table '$table'")
                plugin.errorLog("|")
            } else
            {
                for (column in columns())
                {
                    table.createIFNotExists(column.columnName, column.dataType)
                }
                plugin.infoLog("| successfully created table '$table'")
                plugin.infoLog("|")
            }
        }

        fun columns(): Array<QueueTableColumn>
        {
            return values().filter { it != ID }.toTypedArray()
        }
    }
}