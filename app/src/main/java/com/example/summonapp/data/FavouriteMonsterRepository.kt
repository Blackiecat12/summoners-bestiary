package com.example.summonapp.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavouriteMonsterRepository(private val favouriteMonsterDao: FavouriteMonsterDao) {
    val favouritedMonsterIds: Flow<List<String>> = favouriteMonsterDao.getFavouritedMonsterIds()

    fun isMonsterFavourited(monsterId: String): Flow<Boolean> {
        return favouriteMonsterDao.isMonsterFavourited(monsterId).map { it > 0 }
    }

    suspend fun toggleFavourite(monsterId: String, isFavourited: Boolean) {
        if (isFavourited) {
            favouriteMonsterDao.addFavourite(FavouriteMonster(monsterId))
        } else {
            favouriteMonsterDao.removeFavourite(monsterId)
        }
    }
}