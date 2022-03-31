package ru.gb.searchmovies.data.details

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import ru.gb.searchmovies.BuildConfig
import ru.gb.searchmovies.data.dto.MovieDTO

interface MovieApi {
    @GET("3/movie/{idMovie}?api_key=${BuildConfig.API_KEY}&language=ru-RU")
    fun getMovie(
        @Path("idMovie") idMovie : Int
    ): Call<MovieDTO>
}