package me.sd_master92.customvoting.constants.models

class VoteSiteUUID(uniqueId: String)
{
    private val value = uniqueId.lowercase().replace(".", "_")
    val serviceName = uniqueId.replace("_", ".")

    override fun toString(): String
    {
        return value
    }

    override fun equals(other: Any?): Boolean
    {
        if (this === other) return true
        if (other !is VoteSiteUUID) return false

        return value == other.value
    }

    override fun hashCode(): Int
    {
        var result = value.hashCode()
        result = 31 * result + serviceName.hashCode()
        return result
    }
}