package ru.gb.searchmovies.data.details

import okhttp3.Callback

interface IDetailsRepository {
    fun getMovieDetailsFromServer (requestLink: String, callback : Callback)
}