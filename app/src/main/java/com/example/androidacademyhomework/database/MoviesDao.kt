package com.example.androidacademyhomework.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {

    @Query("SELECT * from Movies")
     fun getAllMovies(): Flow<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movies:List<MovieEntity>)

    @Query("DELETE FROM Movies WHERE Id == :id")
    suspend fun deleteMovie(id:Long)
}