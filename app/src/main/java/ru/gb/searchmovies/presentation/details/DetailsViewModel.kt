package ru.gb.searchmovies.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.gb.searchmovies.data.states.AppState
import ru.gb.searchmovies.data.dto.MovieDTO
import ru.gb.searchmovies.data.details.IDetailsRepository
import ru.gb.searchmovies.data.details.DetailsRepository
import ru.gb.searchmovies.data.details.RemoteDataSource
import ru.gb.searchmovies.data.dto.convertDtoToModel


private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val CORRUPTED_DATA = "Неполные данные"

class DetailsViewModel(
    private val detailsLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val detailsRepository: IDetailsRepository =
        DetailsRepository(RemoteDataSource())
) : ViewModel() {
    val liveDate: LiveData<AppState> get() = detailsLiveData

    fun getMovieFromRemoteSource(idMovie: Int) {
        detailsLiveData.postValue(AppState.Loading)
        detailsRepository.getMovieDetailsFromServer(idMovie, callBack)
    }

    private val callBack = object : Callback<MovieDTO> {
        override fun onResponse(call: Call<MovieDTO>, response: Response<MovieDTO>) {
            val serverResponse: MovieDTO? = response.body()
            detailsLiveData.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    checkResponse(serverResponse)
                } else {
                    AppState.Error(Throwable(SERVER_ERROR))
                }
            )

        }

        override fun onFailure(call: Call<MovieDTO>, t: Throwable) {
            detailsLiveData.postValue(AppState.Error(Throwable(t.message ?: REQUEST_ERROR)))
        }
    }

    private fun checkResponse(serverResponse: MovieDTO): AppState {
        return if (serverResponse.id == null || serverResponse.name.isNullOrEmpty()
            || serverResponse.genres.isEmpty() || serverResponse.runtime.isNullOrEmpty() || serverResponse.releaseDate.isNullOrEmpty()
            || serverResponse.popularity.isNullOrEmpty() || serverResponse.overview.isNullOrEmpty()
        ) {
            AppState.Error(Throwable(CORRUPTED_DATA))
        } else {
            AppState.SuccessDetails(convertDtoToModel(serverResponse))
        }
    }
}
