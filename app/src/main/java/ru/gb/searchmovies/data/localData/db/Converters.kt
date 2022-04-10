package ru.gb.searchmovies.data.localData.db

import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import ru.gb.searchmovies.data.db.HistoryEntity
import ru.gb.searchmovies.data.dto.Movie
import java.util.*

@RequiresApi(Build.VERSION_CODES.N)
fun convertMovieToEntity(movie: Movie, note: String): HistoryEntity {
    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
    return HistoryEntity(
        0, movie.id, movie.name, note,
                sdf.format(Date())
    )
}
