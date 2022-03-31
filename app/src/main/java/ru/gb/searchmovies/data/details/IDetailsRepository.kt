package ru.gb.searchmovies.data.details

import retrofit2.Callback
import ru.gb.searchmovies.data.dto.MovieDTO


interface IDetailsRepository {
    fun getMovieDetailsFromServer (idMovie : Int, callback : Callback<MovieDTO>)
}