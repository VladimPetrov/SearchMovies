package ru.gb.searchmovies.data

import com.google.gson.annotations.SerializedName

data class MovieDTO(
    val id: Int?,
    @SerializedName("title")
    val name: String?,
    val genres: List<Genre>,
    val runtime: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("vote_average")
    val popularity: String?,
    val overview: String?

)
{
    fun showGenres(): String {
        var result: String = ""
        for (i in 0..(genres.count() - 2)) {
            result += genres.get(i).name + ", "
        }
        result += genres.last().name
        return result
    }
}
