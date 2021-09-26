package com.example.androidacademyhomework.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MoviesDao {

    @Query("SELECT * from Movies")
    suspend fun getAllMovies():List<MovieEntity>

    @Insert
    suspend fun insertMovie(movie:MovieEntity)

    @Query("DELETE FROM Movies WHERE Id == :id")
    suspend fun deleteMovie(id:Long)
}