package com.example.androidacademyhomework.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.androidacademyhomework.network.pojopack.CastItem
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Movies")
class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    val id: Long?,
    @ColumnInfo(name = "pgAge")
    val pgAge: Boolean,
    @ColumnInfo(name = "title")
    val title: String?,

    @ColumnInfo(name = "genres")
    val genres: List<String>?,

    @ColumnInfo(name = "duration")
    val runningTime: Int,

    @ColumnInfo(name = "reviews")
    val reviewCount: Int,

    @ColumnInfo(name = "liked")
    val isLiked: Boolean,

    @ColumnInfo(name = "rating")
    val rating: Float,

    @ColumnInfo(name = "posterUrl")
    val imageUrl: String?,

    @ColumnInfo(name = "backdropUrl")
    val detailImageUrl: String,

    @ColumnInfo(name = "story")
    val storyLine: String
   //@ColumnInfo(name = "actors")
    //val actors: List<ActorsEntity>
):Parcelable