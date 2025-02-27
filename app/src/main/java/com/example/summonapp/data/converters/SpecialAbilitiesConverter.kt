package com.example.summonapp.data.converters

import androidx.room.TypeConverter

class SpecialAbilitiesConverter {
    @TypeConverter
    fun fromSpecialAbilitiesMap(specialAbilities: Map<String, String>?): String {
        return specialAbilities?.entries?.joinToString(";") { "${it.key}:${it.value}" } ?: ""
    }

    @TypeConverter
    fun toSpecialAbilitiesMap(data: String): Map<String, String> {
        return if (data.isEmpty()) emptyMap()
        else data.split(";").associate {
            val (key, value) = it.split(":")
            key to value
        }
    }
}
