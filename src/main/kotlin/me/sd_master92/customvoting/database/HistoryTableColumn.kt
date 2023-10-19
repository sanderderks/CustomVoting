package me.sd_master92.customvoting.database

import me.sd_master92.core.database.CustomColumn
import me.sd_master92.core.database.CustomTable
import me.sd_master92.core.errorLog
import me.sd_master92.core.infoLog
import me.sd_master92.customvoting.CV

enum class HistoryTableColumn(
    val columnName: String,
    val dataType: CustomColumn.DataType
)
{
    ID("id", CustomColumn.DataType.INT_PRIMARY),
    UUID("uuid", CustomColumn.DataType.VARCHAR),
    SITE("site", CustomColumn.DataType.VARCHAR),
    TIME("timestamp", CustomColumn.DataType.LONG),
    QUEUED("queued", CustomColumn.DataType.BOOLEAN);

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
                    if (!table.getColumn(column.columnName).create(column.dataType))
                    {
                        plugin.errorLog("| could not create column '${column.columnName}'")
                    }
                }
                plugin.infoLog("| successfully created table '${table.name}'")
                plugin.infoLog("|")
            }
        }

        private fun columns(): Array<HistoryTableColumn>
        {
            return entries.filter { it != ID }.toTypedArray()
        }
    }
}