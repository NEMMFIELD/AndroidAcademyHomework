package com.example.androidacademyhomework.database

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.androidacademyhomework.data.model.viewholder.ResultsItem

@Dao
interface MovieDao
{
   @Query("SELECT * FROM movieslist")
    fun getAll(): List<ResultsItem>

    @Insert(onConflict = REPLACE)
    fun insert(insertedMovie: ResultsItem)

    @Delete
    fun delete(deletedMovie: ResultsItem)
}