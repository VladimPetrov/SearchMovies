package ru.gb.searchmovies.data.localData.db

import android.os.Build
import androidx.annotation.RequiresApi
import ru.gb.searchmovies.data.db.HistoryDao
import ru.gb.searchmovies.data.db.HistoryEntity
import ru.gb.searchmovies.data.dto.Movie

class DbRepository(private val datasource:HistoryDao):IDbRepository {
    override fun getAllHistory(): List<HistoryEntity> = datasource.all()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun saveEntity(movie: Movie, note: String) {
        datasource.insert(convertMovieToEntity(movie, note))
    }
}