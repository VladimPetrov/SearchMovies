package ru.gb.searchmovies.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.gb.searchmovies.data.details.RemoteDataSource
import ru.gb.searchmovies.data.dto.*
import ru.gb.searchmovies.data.listdto.IListRepository
import ru.gb.searchmovies.data.listdto.ListRepository
import ru.gb.searchmovies.data.states.AppState
import ru.gb.searchmovies.data.localData.IRepository
import ru.gb.searchmovies.data.localData.Repository

import java.lang.Thread.sleep

private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val CORRUPTED_DATA = "Неполные данные"

class MainViewModel(
    private val mutableLiveData: MutableLiveData<AppState> = MutableLiveData(),
    //private val repository: IRepository = Repository()
    private val repository: IListRepository = ListRepository(RemoteDataSource())
) : ViewModel() {

    val liveDate: LiveData<AppState> get() = mutableLiveData

    private val callback = object : Callback <ListMovieApi> {
        override fun onResponse(call: Call<ListMovieApi>, response: Response<ListMovieApi>) {
            val serverResponse: ListMovieApi? = response.body()
            mutableLiveData.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    checkResponse(serverResponse)
                } else {
                    AppState.Error(Throwable(SERVER_ERROR))
                }
            )
        }

        override fun onFailure(call: Call<ListMovieApi>, t: Throwable) {
            mutableLiveData.postValue(AppState.Error(Throwable(t.message ?: REQUEST_ERROR)))
        }
    }


    fun getMovieFromLocalSource(isMovies: Boolean) = getDataFromLocalSource(isMovies)



    private fun checkResponse(serverResponse: ListMovieApi): AppState {
        val fact = serverResponse
        return if (fact?.listMoveApi.isEmpty()) {
            AppState.Error(Throwable(CORRUPTED_DATA))
        } else {
            AppState.Success(convertListApiToListModel(serverResponse))
        }
    }

    private fun getDataFromLocalSource(isMovies: Boolean) {
        mutableLiveData.postValue(AppState.Loading)
            if (isMovies) {
                //mutableLiveData.postValue(AppState.Success(repository.getMovieFromLocalStorage()))
                repository.getMovieListFromServer("приключения",callback)
            } else {
                repository.getMovieListFromServer("мультфильм",callback)
            }
    }


}