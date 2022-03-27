package ru.gb.searchmovies.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import ru.gb.searchmovies.data.states.AppState
import ru.gb.searchmovies.data.dto.Movie
import ru.gb.searchmovies.data.dto.MovieDTO
import ru.gb.searchmovies.data.details.IDetailsRepository
import ru.gb.searchmovies.data.details.DetailsRepository
import ru.gb.searchmovies.data.details.RemoteDataSource
import ru.gb.searchmovies.data.dto.convertDtoToModel
import java.io.IOException

private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val CORRUPTED_DATA = "Неполные данные"

class DetailsViewModel (private val detailsLiveData : MutableLiveData<AppState> = MutableLiveData(),
                        private val detailsRepository: IDetailsRepository =
                            DetailsRepository(RemoteDataSource())
):ViewModel() {
    val liveDate: LiveData<AppState> get() = detailsLiveData

    fun getMovieFromRemoteSource(requestLink: String) {
        detailsLiveData.postValue(AppState.Loading)
        detailsRepository.getMovieDetailsFromServer(requestLink, callBack)
    }

    private val callBack = object : Callback {

        @Throws(IOException::class)
        override fun onResponse(call: Call?, response: Response) {
            val serverResponse: String? = response.body()?.string()
            detailsLiveData.postValue(
            if (response.isSuccessful && serverResponse != null) {
                checkResponse(serverResponse)
            } else {
                AppState.Error(Throwable(SERVER_ERROR))
            }
            )
        }
        override fun onFailure(call: Call?, e: IOException?) {
            detailsLiveData.postValue(
                AppState.Error(Throwable(e?.message ?:
            REQUEST_ERROR)))
        }
        private fun checkResponse(serverResponse: String): AppState {
            val movieDTO: MovieDTO =
                Gson().fromJson(serverResponse, MovieDTO::class.java)
            val fact = movieDTO
            return if (fact == null || fact.id == null || fact.name.isNullOrEmpty() ==
                null || fact.runtime.isNullOrEmpty() || fact.overview.isNullOrEmpty() ||
                    fact.popularity.isNullOrEmpty() || fact.releaseDate.isNullOrEmpty() ||
                    fact.runtime.isNullOrEmpty() || fact.genres.isEmpty()) {
                AppState.Error(Throwable(CORRUPTED_DATA))
            } else {
                AppState.SuccessDetails(convertDtoToModel(movieDTO))
            }
        }


    }

}