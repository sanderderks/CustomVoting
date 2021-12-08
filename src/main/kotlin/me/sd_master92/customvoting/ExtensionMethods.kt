package me.sd_master92.customvoting

fun String.appendWhenTrue(value: Boolean, append: String): String
{
    if (value)
    {
        return this + append
    }
    return this
}