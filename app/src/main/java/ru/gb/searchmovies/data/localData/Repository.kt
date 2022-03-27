package ru.gb.searchmovies.data.localData

import ru.gb.searchmovies.data.dto.Movie
import ru.gb.searchmovies.data.dto.getMovieList
import ru.gb.searchmovies.data.dto.getMultList

class Repository : IRepository {
    override fun getMovieFromServer(): Movie {
        return Movie()
    }

    override fun getMovieFromLocalStorage(): List<Movie> = getMovieList()
    override fun getMultFromLocalStorage(): List<Movie> = getMultList()

}