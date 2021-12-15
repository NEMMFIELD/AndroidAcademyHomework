package com.example.androidacademyhomework.database


import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {
    //GET
    @Query("SELECT * from Movies WHERE type LIKE :listType")
    fun getAllMovies(listType: String): Flow<List<MovieEntity>>

    @Query("SELECT * from Movies")
    fun getMovies(): List<MovieEntity>

    @Query("SELECT * from Movies WHERE Id == :id")
    fun getMoviebyId(id: Long): MovieEntity

    @Query("SELECT * from Movies WHERE liked == :liked")
    fun getFavouritesMovies(liked: Boolean): Flow<List<MovieEntity>>

    //INSERT
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovie(movies: List<MovieEntity>)

    //DELETE
    @Query("DELETE FROM Movies WHERE Id == :id")
    suspend fun deleteMovie(id: Long)

    @Query("DELETE FROM Movies")
    suspend fun deleteAll()

    //UPDATE
    @Update
    suspend fun updateLike(movie: MovieEntity)
}