/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.summonapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.summonapp.data.converters.AbilityScoreConverter
import com.example.summonapp.data.converters.ArmourClassConverter
import com.example.summonapp.data.converters.AttackBonusConverter
import com.example.summonapp.data.converters.HealthConverter
import com.example.summonapp.data.converters.ListStringConverter
import com.example.summonapp.data.converters.SpecialAbilitiesConverter
import com.example.summonapp.data.converters.SpeedConverter
import com.example.summonapp.models.AbilityScore
import com.example.summonapp.models.ArmourClass
import com.example.summonapp.models.AttackBonus
import com.example.summonapp.models.Health
import com.example.summonapp.models.enums.Alignment
import com.example.summonapp.models.enums.Size

/**
 * Entity data class represents a single row in the database.
 */
@Entity(tableName = "monsters")
data class Monster(
    @PrimaryKey val name: String, // Unique identifier
    val summonLevel: Int,
    val cr: String,
    val size: Size,
    val alignment: Alignment,
    val creatureType: String,
    @TypeConverters(ListStringConverter::class) val creatureSubtypes: List<String>,
    val initiative: Int,
    val perception: Int,
    @TypeConverters(ListStringConverter::class) val senses: List<String>,
    @TypeConverters(HealthConverter::class) val health: Health,
    @TypeConverters(ArmourClassConverter::class) val armourClass: ArmourClass,
    @TypeConverters(SpeedConverter::class) val speed: Map<String, Int>,
    val meleeAttacks: String?,
    val rangedAttacks: String?,
    @TypeConverters(AbilityScoreConverter::class) val abilityScores: AbilityScore,
    @TypeConverters(AttackBonusConverter::class) val attackBonus: AttackBonus,
    @TypeConverters(ListStringConverter::class) val specialQualities: List<String>?,
    @TypeConverters(SpecialAbilitiesConverter::class) val specialAbilities: Map<String, String>?
)
