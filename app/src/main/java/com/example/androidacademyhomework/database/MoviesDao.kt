package com.example.androidacademyhomework.database


import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {

    @Query("SELECT * from Movies WHERE type LIKE :listType")
     fun getAllMovies(listType:String): Flow<List<MovieEntity>>

     @Query("SELECT * from Movies")
     fun getMovies():List<MovieEntity>

     @Query("SELECT * from Movies WHERE Id == :id")
     fun getMoviebyId(id:Long):MovieEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovie(movies:List<MovieEntity>)

    @Query("DELETE FROM Movies WHERE Id == :id")
    suspend fun deleteMovie(id:Long)

    @Query("DELETE FROM Movies")
    suspend fun deleteAll()

   @Update
    suspend fun updateLike(movie:MovieEntity)
}