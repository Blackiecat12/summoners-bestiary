package com.example.summonapp.data.converters

import androidx.room.TypeConverter
import com.example.summonapp.models.enums.Size

class SizeConverter {
    @TypeConverter
    fun fromSize(size: Size?): String? {
        return size?.name // Converts Enum to String
    }

    @TypeConverter
    fun toSize(value: String?): Size? {
        return value?.let { enumValueOf<Size>(it) } // Converts String back to Enum
    }
}
