package ru.gb.searchmovies.data.states

import ru.gb.searchmovies.data.db.HistoryEntity
import ru.gb.searchmovies.data.dto.Movie

sealed class AppState {
    data class Success(val movie: List<Movie>) : AppState()
    data class SuccessHistory(val historyList: List<HistoryEntity>) : AppState()
    data class SuccessDetails(val movie: Movie) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}

