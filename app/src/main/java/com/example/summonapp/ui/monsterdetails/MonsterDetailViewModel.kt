package com.example.summonapp.ui.monsterdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.summonapp.data.Monster
import com.example.summonapp.data.MonsterRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MonsterDetailViewModel(
    savedStateHandle: SavedStateHandle,
    repository: MonsterRepository
) : ViewModel() {
    val monsterId: String = checkNotNull(savedStateHandle[MonsterDetailDestination.monsterIdArg])

    val monster: StateFlow<MonsterDetailUiState> = repository.getMonsterStream(monsterId)
        .filterNotNull()
        .map { MonsterDetailUiState(monsterId, it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = MonsterDetailUiState(monsterId, null)
        )


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class MonsterDetailUiState(val monsterId: String, val monster: Monster?)