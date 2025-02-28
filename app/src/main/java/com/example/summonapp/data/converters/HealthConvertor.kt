package com.example.summonapp.data.converters

import androidx.room.TypeConverter
import com.example.summonapp.models.Health

class HealthConverter {
    @TypeConverter
    fun fromHealth(health: Health): String {
        return listOf(
            health.total,
            health.hitDice
        ).joinToString(",")
    }

    @TypeConverter
    fun toHealth(data: String): Health {
        val values = data.split(",").map { it }
        return Health(
            total = values[0].toInt(),
            hitDice = values[1]
        )
    }
}
