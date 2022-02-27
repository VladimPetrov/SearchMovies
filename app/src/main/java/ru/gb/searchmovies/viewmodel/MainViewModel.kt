package ru.gb.searchmovies.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.gb.searchmovies.data.AppState
import ru.gb.searchmovies.data.IRepository
import ru.gb.searchmovies.data.Repository
import java.lang.Thread.sleep

class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: IRepository = Repository()
) : ViewModel() {

    fun getLiveData() = liveDataToObserve

    fun getMovieFromLocalSource(isMovies: Boolean) = getDataFromLocalSource(isMovies)

    private fun getDataFromLocalSource(isMovies: Boolean) {
        liveDataToObserve.postValue(AppState.Loading)
        Thread {
            sleep(2000)
            if (isMovies) {
                liveDataToObserve.postValue(AppState.Success(repository.getMovieFromLocalStorage()))
            } else {
                liveDataToObserve.postValue(AppState.Success(repository.getMultFromLocalStorage()))
            }
        }.start()
    }
}