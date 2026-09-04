package com.rola.app.data.local.database

import androidx.room.TypeConverter

class StringListConverter {
    @TypeConverter
    fun fromList(values: List<String>): String = values.joinToString(separator = "||")

    @TypeConverter
    fun toList(value: String): List<String> = value
        .takeIf { it.isNotBlank() }
        ?.split("||")
        .orEmpty()
}
