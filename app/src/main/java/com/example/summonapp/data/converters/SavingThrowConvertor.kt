package com.example.summonapp.data.converters

import androidx.room.TypeConverter
import com.example.summonapp.models.SavingThrows

class SavingThrowsConvertor {

    @TypeConverter
    fun fromSavingThrows(savingThrow: SavingThrows): String {
        return listOf(
            savingThrow.fortitude,
            savingThrow.reflex,
            savingThrow.will
        ).joinToString(",")
    }

    @TypeConverter
    fun toSavingThrows(data: String): SavingThrows {
        val values = data.split(",").map { it.toInt() }
        return SavingThrows(
            fortitude = values[0],
            reflex = values[1],
            will = values[2]
        )
    }

}