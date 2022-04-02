package ru.gb.searchmovies.data.listdto

import retrofit2.Callback
import ru.gb.searchmovies.data.details.RemoteDataSource
import ru.gb.searchmovies.data.dto.ListMovieApi
import ru.gb.searchmovies.data.dto.MovieDTO

class ListRepository(private val remoteDataSource: RemoteDataSource) : IListRepository {
    override fun getMovieListFromServer(query: String, isAdultMovie:Boolean, callback: Callback<ListMovieApi>) {
        remoteDataSource.getMovieList(query, isAdultMovie, callback)
    }
}