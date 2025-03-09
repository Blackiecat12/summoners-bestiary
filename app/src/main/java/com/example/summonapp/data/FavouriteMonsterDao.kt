package com.example.summonapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteMonsterDao {
    @Query("SELECT monsterId FROM favourite_monsters")
    fun getFavouritedMonsterIds(): Flow<List<String>>  // Live updates

    @Query("SELECT COUNT(*) FROM favourite_monsters WHERE monsterId = :monsterId")
    fun isMonsterFavourited(monsterId: String): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavourite(favouriteMonster: FavouriteMonster)

    @Query("DELETE FROM favourite_monsters WHERE monsterId = :monsterId")
    suspend fun removeFavourite(monsterId: String)
}