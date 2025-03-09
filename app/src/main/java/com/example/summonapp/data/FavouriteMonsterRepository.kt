package com.example.summonapp.data

import kotlinx.coroutines.flow.Flow

class FavouriteMonsterRepository(private val favouriteMonsterDao: FavouriteMonsterDao) {
    val favouritedMonsterIds: Flow<List<String>> = favouriteMonsterDao.getFavouritedMonsterIds()

    suspend fun toggleFavourite(monsterId: String, isFavourited: Boolean) {
        if (isFavourited) {
            favouriteMonsterDao.addFavourite(FavouriteMonster(monsterId))
        } else {
            favouriteMonsterDao.removeFavourite(monsterId)
        }
    }
}