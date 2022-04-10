package ru.gb.searchmovies.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val idMovie: Int,
    val name: String,
    val note: String,
    val date: String
)
