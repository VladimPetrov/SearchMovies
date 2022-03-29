package ru.gb.searchmovies.data.details

import com.google.gson.GsonBuilder

import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.gb.searchmovies.data.dto.ListMovieApi
import ru.gb.searchmovies.data.dto.MovieDTO

class RemoteDataSource {

    private val movieApi = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .build().create(MovieApi::class.java)


    fun getMovieDetails(idMovie: Int, callback: Callback<MovieDTO>) {
        movieApi.getMovie(idMovie).enqueue(callback)
    }
    fun getMovieList(query : String, callback: Callback<ListMovieApi>) {
        movieApi.getListMovie(query = query).enqueue(callback)
    }
}