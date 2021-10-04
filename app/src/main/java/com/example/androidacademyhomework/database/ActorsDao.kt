package com.example.androidacademyhomework.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ActorsDao {
    @Query("SELECT * FROM actorslist WHERE filmId=:filmId")
    fun getAllActors(filmId:Long): Flow<List<ActorsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertActors(actors: List<ActorsEntity?>?)
}