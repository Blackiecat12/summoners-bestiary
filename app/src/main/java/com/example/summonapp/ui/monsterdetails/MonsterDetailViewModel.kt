package com.example.summonapp.ui.monsterdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.summonapp.data.FavouriteMonsterRepository
import com.example.summonapp.data.Monster
import com.example.summonapp.data.MonsterRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MonsterDetailViewModel(
    savedStateHandle: SavedStateHandle,
    monsterRepo: MonsterRepository,
    private val favouriteMonsterRepo: FavouriteMonsterRepository
) : ViewModel() {
    val monsterId: String = checkNotNull(savedStateHandle[MonsterDetailDestination.monsterIdArg])

    val monster: StateFlow<MonsterDetailUiState> = monsterRepo.getMonsterStream(monsterId)
        .filterNotNull()
        .map { MonsterDetailUiState(monsterId, it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = MonsterDetailUiState(monsterId, null)
        )

    val isFavourited: StateFlow<Boolean> = favouriteMonsterRepo.isMonsterFavourited(monsterId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(TIMEOUT_MILLIS), false)

    // Function to toggle favourite status
    fun toggleFavourite() {
        viewModelScope.launch {
            val currentState = isFavourited.value
            favouriteMonsterRepo.toggleFavourite(monsterId, !currentState)
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class MonsterDetailUiState(
    val monsterId: String,
    val monster: Monster?,
    val isFavourited: Boolean = false
)