package me.sd_master92.customvoting.constants.models

import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URI
import java.net.http.HttpClient.*
import java.util.*

data class BStatsData(
    val name: String,
    val y: Int
)
{
    companion object
    {
        private var lastChecked: MutableMap<String, Calendar> = mutableMapOf()
        var VOTE_SITES: List<BStatsData> = listOf()
            private set
            get()
            {
                fetchData(field, "vote_sites")?.let { field = it }
                return field
            }

        var MINECRAFT_VERSIONS: List<BStatsData> = listOf()
            private set
            get()
            {
                fetchData(field, "minecraftVersion")?.let { field = it }
                return field
            }

        var COUNTRIES: List<BStatsData> = listOf()
            private set
            get()
            {
                fetchData(field, "location")?.let { field = it }
                return field
            }

        fun refresh()
        {
            lastChecked.clear()
            VOTE_SITES
            MINECRAFT_VERSIONS
            COUNTRIES
        }

        private fun fetchData(field: List<BStatsData>, chartId: String): List<BStatsData>?
        {
            if (field.isEmpty()
                || !lastChecked.containsKey(chartId)
                || Calendar.getInstance()[Calendar.DAY_OF_YEAR] != lastChecked[chartId]!![Calendar.DAY_OF_YEAR]
                || Calendar.getInstance()[Calendar.HOUR_OF_DAY] - lastChecked[chartId]!![Calendar.HOUR_OF_DAY] >= 1
            )
            {
                lastChecked[chartId] = Calendar.getInstance()
                return getBStatsData(chartId)
            }
            return null
        }

        private fun getBStatsData(chartId: String): List<BStatsData>
        {
            val data = try
            {
                val apiUrl = "https://bstats.org/api/v1/plugins/13544/charts/$chartId/data"
                val uri = URI(apiUrl)
                val connection = uri.toURL().openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                BufferedReader(InputStreamReader(connection.inputStream)).readLine()
            } catch (e: Exception)
            {
                null
            }
            return Gson().fromJson(data, Array<BStatsData>::class.java)?.toList() ?: listOf()
        }
    }
}