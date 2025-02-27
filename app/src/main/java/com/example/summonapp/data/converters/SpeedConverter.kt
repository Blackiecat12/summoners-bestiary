package com.example.summonapp.data.converters

import androidx.room.TypeConverter

class SpeedConverter {
    @TypeConverter
    fun fromSpeedMap(speed: Map<String, Int>?): String {
        return speed?.entries?.joinToString(";") { "${it.key}:${it.value}" } ?: ""
    }

    @TypeConverter
    fun toSpeedMap(data: String): Map<String, Int> {
        return if (data.isEmpty()) emptyMap()
        else data.split(";").associate {
            val (key, value) = it.split(":")
            key to value.toInt()
        }
    }
}
