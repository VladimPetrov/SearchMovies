package ru.gb.searchmovies.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.gb.searchmovies.data.states.AppState
import ru.gb.searchmovies.data.localData.IRepository
import ru.gb.searchmovies.data.localData.Repository
import java.lang.Thread.sleep

class MainViewModel(
    private val mutableLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: IRepository = Repository()
) : ViewModel() {

    val liveDate: LiveData<AppState> get() = mutableLiveData

    fun getMovieFromLocalSource(isMovies: Boolean) = getDataFromLocalSource(isMovies)

    private fun getDataFromLocalSource(isMovies: Boolean) {
        mutableLiveData.postValue(AppState.Loading)
        Thread {
            sleep(1000)
            if (isMovies) {
                mutableLiveData.postValue(AppState.Success(repository.getMovieFromLocalStorage()))
            } else {
                mutableLiveData.postValue(AppState.Success(repository.getMultFromLocalStorage()))
            }
        }.start()
    }
}