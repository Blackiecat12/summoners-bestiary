package com.example.summonapp.data.converters

import androidx.room.TypeConverter
import com.example.summonapp.models.DamageResist

class DamageResistConvertor {

    @TypeConverter
    fun fromDamageResist(damageResist: DamageResist): String {
        return listOf(
            damageResist.value,
            damageResist.bypass
        ).joinToString(",")
    }

    @TypeConverter
    fun toDamageResist(data: String): DamageResist {
        val values = data.split(",").map { it }
        return DamageResist(
            value = values[0].toInt(),
            bypass = values[1]
        )
    }

}