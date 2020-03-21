package com.erank.nsappsmoviesapi.utils

import androidx.room.TypeConverter

object ArrayConverter {

    @TypeConverter
    @JvmStatic
    fun arrayToStoredString(strings: Array<String>) = strings.joinToString(";")

    @TypeConverter
    @JvmStatic
    fun storedStringToArray(value: String) =
        value.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
}
