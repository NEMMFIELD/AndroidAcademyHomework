package com.example.androidacademyhomework.database.actor

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.example.androidacademyhomework.database.movie.MovieDb

@Entity(tableName = "actorslist",
foreignKeys = [ForeignKey(
    entity = MovieDb::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("filmId"),
    onDelete = CASCADE
)],
    indices = [Index(value = ["filmId"])]
)
data class ActorDb(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id:Long,
    val filmId:Long,
    @ColumnInfo(name = "name")
    val name:String?,
    @ColumnInfo(name = "profile_path")
    val profilePath: String?
)
