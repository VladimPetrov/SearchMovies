package ru.gb.searchmovies.data.localData.db

import ru.gb.searchmovies.data.db.HistoryEntity
import ru.gb.searchmovies.data.dto.Movie

interface IDbRepository {
    fun getAllHistory(): List<HistoryEntity>
    fun saveEntity(movie: Movie)
}