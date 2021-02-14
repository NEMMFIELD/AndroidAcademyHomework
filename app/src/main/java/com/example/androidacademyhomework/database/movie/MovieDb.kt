package com.example.androidacademyhomework.database.movie

import androidx.room.*


@Entity(tableName = "movieslist")
data class MovieDb(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long? = null,

    @ColumnInfo(name = "secure_base_url")
    val secureBaseUrl: String? = null,

    @ColumnInfo(name = "poster_path")
    val posterPath: String? = null,

    @ColumnInfo(name = "title")
    val title: String? = null,

    @ColumnInfo(name = "vote_average")
    val voteAverage: Float? = null,

    @ColumnInfo(name = "genres")
    val genres: List<String>
)
