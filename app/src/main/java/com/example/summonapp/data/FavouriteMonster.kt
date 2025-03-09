package com.example.summonapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_monsters")
data class FavouriteMonster (
    @PrimaryKey
    val monsterId: String
)
