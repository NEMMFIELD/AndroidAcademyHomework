package com.example.androidacademyhomework.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "movieslist")
data class MovieDb(
    @ColumnInfo(name = "title")
    val title: String? = null,

    @ColumnInfo(name = "poster_path")
    val posterPath: String? = null,

    @ColumnInfo(name = "vote_average")
    val voteAverage: Float? = null,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int? = null,

    @ColumnInfo(name = "secure_base_url")
    val secureBaseUrl: String? = null
)
