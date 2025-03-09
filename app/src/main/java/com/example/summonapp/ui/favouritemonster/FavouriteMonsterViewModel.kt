package com.example.summonapp.ui.favouritemonster

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.summonapp.data.FavouriteMonsterRepository
import com.example.summonapp.data.Monster
import com.example.summonapp.data.MonsterRepository
import com.example.summonapp.ui.home.HomeUiState
import com.example.summonapp.ui.home.HomeViewModel
import com.example.summonapp.ui.home.HomeViewModel.Companion
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


class FavouriteMonsterViewModel(
    monsterRepo: MonsterRepository,
    favouriteMonsterRepo: FavouriteMonsterRepository
) : ViewModel() {

    private val favouritedMonsterNames: Flow<List<String>> = favouriteMonsterRepo.favouritedMonsterIds
        .map { ids -> ids.map { it.toString() } } // Convert IDs to Strings if necessary

    // Retrieve the full monster details for each favourited monster
    @OptIn(ExperimentalCoroutinesApi::class)
    val favouritedMonsters: StateFlow<List<Monster>> = favouritedMonsterNames
        .flatMapLatest { names ->
            combine(names.map { monsterRepo.getMonsterStream(it) }) { monsters ->
                monsters.filterNotNull() // Remove null values
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}