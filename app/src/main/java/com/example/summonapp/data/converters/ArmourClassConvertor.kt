package com.example.summonapp.data.converters

import androidx.room.TypeConverter
import com.example.summonapp.models.ArmourClass

class ArmourClassConverter {
    @TypeConverter
    fun fromArmourClass(armourClass: ArmourClass): String {
        return listOf(
            armourClass.base,
            armourClass.touch,
            armourClass.flatFooted
        ).joinToString(",")
    }

    @TypeConverter
    fun toArmourClass(data: String): ArmourClass {
        val values = data.split(",").map { it.toInt() }
        return ArmourClass(
            base = values[0],
            touch = values[1],
            flatFooted = values[2]
        )
    }
}
