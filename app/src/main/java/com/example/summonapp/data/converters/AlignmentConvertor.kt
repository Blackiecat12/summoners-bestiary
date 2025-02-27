package com.example.summonapp.data.converters

import androidx.room.TypeConverter
import com.example.summonapp.models.enums.Alignment

class AlignmentConverter {
    @TypeConverter
    fun fromAlignment(alignment: Alignment): String {
        return alignment.name // Converts Enum to String
    }

    @TypeConverter
    fun toAlignment(value: String): Alignment {
        return enumValueOf<Alignment>(value) // Converts String back to Enum
    }
}
