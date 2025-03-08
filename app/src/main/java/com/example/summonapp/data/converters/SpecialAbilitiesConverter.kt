package com.example.summonapp.data.converters

import android.util.Log
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken

class SpecialAbilitiesConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromSpecialAbilitiesMap(specialAbilities: Map<String, String>?): String {
        return gson.toJson(specialAbilities) // Convert Map to JSON string
    }

    @TypeConverter
    fun toSpecialAbilitiesMap(data: String): Map<String, String> {
        return when {
            data.isEmpty() -> emptyMap() // Handle null or empty strings safely
            data.trim().startsWith("{") -> { // Likely a JSON object
                try {
                    gson.fromJson(data, object : TypeToken<Map<String, String>>() {}.type)
                } catch (e: JsonSyntaxException) {
                    emptyMap() // Fallback if the JSON is corrupt
                }
            }
            else -> { // Handle legacy plain string format (fallback)
                data.split(";").mapNotNull {
                    val parts = it.split(":", limit = 2) // Split on the first colon only
                    if (parts.size == 2) parts[0].trim() to parts[1].trim() else null
                }.toMap()
            }
        }
    }
}
