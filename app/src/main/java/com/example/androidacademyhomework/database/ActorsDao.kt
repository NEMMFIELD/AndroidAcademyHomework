package com.example.androidacademyhomework.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ActorsDao {
    @Query("SELECT * FROM actorslist WHERE filmId=:movieId")
    fun getAllActors(movieId:Long): List<ActorsEntity> //Список актеров фильма

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertActors(actors: List<ActorsEntity?>?)
}