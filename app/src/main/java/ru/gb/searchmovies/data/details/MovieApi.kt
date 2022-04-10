package ru.gb.searchmovies.data.details

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.gb.searchmovies.BuildConfig
import ru.gb.searchmovies.data.dto.ListMovieApi
import ru.gb.searchmovies.data.dto.MovieDTO

interface MovieApi {
    @GET("3/movie/{idMovie}")
    fun getMovie(
        @Path("idMovie") idMovie : Int,
        @Query("api_key") apiKey : String = BuildConfig.API_KEY,
        @Query("language") language : String = "ru-RU"
    ): Call<MovieDTO>

    @GET("3/search/movie")
    fun getListMovie(
        @Query("api_key") apiKey : String = BuildConfig.API_KEY,
        @Query("language") language : String = "ru-RU",
        @Query("page") page : Int = 1,
        @Query("include_adult") includeAdult : String = "false",
        @Query("query") query : String
    ): Call<ListMovieApi>
}