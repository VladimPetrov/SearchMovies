package ru.gb.searchmovies.data.dto

import com.google.gson.annotations.SerializedName

data class ListMovieApi(
    val page : Int,
    @SerializedName("results")
    val listMoveApi : List<MovieAPI>
)

 fun convertListApiToListModel(listMovieApi: ListMovieApi): List<Movie> {
    val fact: List<MovieAPI> = listMovieApi.listMoveApi!!
    var model = mutableListOf<Movie>()
    for(movieApi in fact) {
        model.add(
            convertApiToModel(movieApi)
        )
    }
    return model

}