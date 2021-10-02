package com.example.androidacademyhomework.database

import android.os.Parcelable
import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "actorslist",
    foreignKeys = [ForeignKey(
        entity = MovieEntity::class,
        parentColumns = arrayOf("Id"),
        childColumns = arrayOf("filmId"),
        onDelete = CASCADE
    )],
    indices = [Index(value = ["filmId"])]
)
@Parcelize
data class ActorsEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    val filmId: Long,
    @ColumnInfo(name = "name")
    val name: String?,
    @ColumnInfo(name = "profile_path")
    val profilePath: String?
):Parcelable