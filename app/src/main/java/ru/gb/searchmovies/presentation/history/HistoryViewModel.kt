package ru.gb.searchmovies.presentation.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.gb.searchmovies.App.Companion.getHistoryDao
import ru.gb.searchmovies.data.localData.db.DbRepository
import ru.gb.searchmovies.data.localData.db.IDbRepository
import ru.gb.searchmovies.data.states.AppState

class HistoryViewModel (
    val historyLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val historyRepository: IDbRepository =
        DbRepository(getHistoryDao())
) : ViewModel() {
    fun getAllHistory() {
        historyLiveData.postValue(AppState.Loading)
        historyLiveData.postValue(
            AppState.SuccessHistory(historyRepository.getAllHistory())
        )
    }

}