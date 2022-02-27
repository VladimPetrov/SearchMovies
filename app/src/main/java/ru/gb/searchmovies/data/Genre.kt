package ru.gb.searchmovies.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Genre(
    val genre: List<String> = listOf("драма")
) : Parcelable {
    override fun toString(): String {
        var result: String = ""
        for (i in 0..(genre.count() - 2)) {
            result += genre.get(i) + ", "
        }
        result += genre.last()
        return result
    }
}