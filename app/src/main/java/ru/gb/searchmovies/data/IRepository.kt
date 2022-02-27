package ru.gb.searchmovies.data

interface IRepository {
    fun getMovieFromServer(): Movie
    fun getMovieFromLocalStorage(): List<Movie>
    fun getMultFromLocalStorage(): List<Movie>
}