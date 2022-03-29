package ru.gb.searchmovies.data.listdto

import retrofit2.Callback
import ru.gb.searchmovies.data.dto.ListMovieApi
import ru.gb.searchmovies.data.dto.MovieDTO

interface IListRepository {
    fun  getMovieListFromServer (query : String, callback: Callback<ListMovieApi>)
}