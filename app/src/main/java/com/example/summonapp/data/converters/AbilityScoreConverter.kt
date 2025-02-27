package com.example.summonapp.data.converters

import androidx.room.TypeConverter
import com.example.summonapp.models.AbilityScore

class AbilityScoreConverter {
    @TypeConverter
    fun fromAbilityScore(abilityScore: AbilityScore): String {
        return listOf(
            abilityScore.strength,
            abilityScore.dexterity,
            abilityScore.constitution,
            abilityScore.intelligence,
            abilityScore.wisdom,
            abilityScore.charisma
        ).joinToString(",")
    }

    @TypeConverter
    fun toAbilityScore(data: String): AbilityScore {
        val values = data.split(",").map { it.toInt() }
        return AbilityScore(
            strength = values[0],
            dexterity = values[1],
            constitution = values[2],
            intelligence = values[3],
            wisdom = values[4],
            charisma = values[5]
        )
    }
}
