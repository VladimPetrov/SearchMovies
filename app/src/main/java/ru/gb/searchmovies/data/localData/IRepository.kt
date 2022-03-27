package ru.gb.searchmovies.data.localData

import ru.gb.searchmovies.data.dto.Movie

interface IRepository {
    fun getMovieFromServer(): Movie
    fun getMovieFromLocalStorage(): List<Movie>
    fun getMultFromLocalStorage(): List<Movie>
}