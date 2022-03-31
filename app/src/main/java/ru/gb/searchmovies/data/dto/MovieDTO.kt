package ru.gb.searchmovies.data.dto

import com.google.gson.annotations.SerializedName

data class MovieDTO(
    val id: Int?,
    @SerializedName("title")
    val name: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    val genres: List<Genre>,
    val runtime: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("vote_average")
    val popularity: String?,
    val overview: String?

)


fun convertDtoToModel(movieDTO: MovieDTO): Movie {
    val fact: MovieDTO = movieDTO
    return Movie(
        fact.id!!, fact.name!!, fact.posterPath!!, fact.genres, fact.runtime!!,
        fact.releaseDate!!, fact.popularity!!, fact.overview!!
    )

}