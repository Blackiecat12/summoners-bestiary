package com.example.summonapp.data.converters

import androidx.room.TypeConverter
import com.example.summonapp.models.AttackBonus

class AttackBonusConverter {
    @TypeConverter
    fun fromAttackBonus(attackBonus: AttackBonus): String {
        return listOf(
            attackBonus.base,
            attackBonus.cmd,
            attackBonus.cmb
        ).joinToString(",")
    }

    @TypeConverter
    fun toAttackBonus(data: String): AttackBonus {
        val values = data.split(",").map { it.toInt() }
        return AttackBonus(
            base = values[0],
            cmd = values[1],
            cmb = values[2]
        )
    }
}
