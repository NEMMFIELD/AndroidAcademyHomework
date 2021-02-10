package com.example.androidacademyhomework.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query


@Dao
interface MovieDao {
    @Query("SELECT * FROM movieslist")
    fun getAll(): LiveData<List<MovieDb>>

    @Insert(onConflict = IGNORE)
    fun addMovie(movie: MovieDb)

    @Insert
    fun insertAll(films: List<MovieDb>)


    @Delete
    fun delete(deletedMovie: MovieDb)
}