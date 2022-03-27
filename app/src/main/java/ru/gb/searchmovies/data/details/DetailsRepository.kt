package ru.gb.searchmovies.data.details

import okhttp3.Callback

class DetailsRepository(private val remoteDataSource: RemoteDataSource) : IDetailsRepository {
    override fun getMovieDetailsFromServer(requestLink: String, callback: Callback) {
        remoteDataSource.getWeatherDetails(requestLink, callback)
    }
}