package ru.gb.searchmovies.data.dto

import com.google.gson.annotations.SerializedName

data class MovieAPI(
    val id: Int?,
    @SerializedName("title")
    val name: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("genre_ids")
    val genresIds: List<Int>,
    @SerializedName("vote_average")
    val popularity: String?
)

fun convertApiToModel(movieApi: MovieAPI): Movie {
    val popularity = movieApi.popularity ?: "0.0"
    val posterPath = movieApi.posterPath ?: ""
    return Movie(
        movieApi.id!!, movieApi.name!!, posterPath, convertGengeIds(movieApi.genresIds), "",
        "", popularity, ""
    )

}

fun convertGengeIds(genreIds: List<Int>): List<Genre> {
    val fact: List<Int> = genreIds
    val genre = mutableListOf<Genre>()
    var name: String
    for (idGenre in fact) {
        when (idGenre) {
            12 -> name = "приключения"
            14 -> name = "фэнтези"
            16 -> name = "мультфильм"
            18 -> name = "драма"
            27 -> name = "ужасы"
            28 -> name = "боевик"
            35 -> name = "комедия"
            36 -> name = "история"
            37 -> name = "вестерн"
            53 -> name = "триллер"
            80 -> name = "криминал"
            99 -> name = "документальный"
            878 -> name = "фантастика"
            9648 -> name = "детектив"
            10402 -> name = "музыка"
            10749 -> name = "мелодрама"
            10751 -> name = "семейный"
            10752 -> name = "военный"
            10770 -> name = "телевизионный фильм"
            else -> name = "неизвестный"
        }
        genre.add(Genre(idGenre, name))
    }
    return genre
}