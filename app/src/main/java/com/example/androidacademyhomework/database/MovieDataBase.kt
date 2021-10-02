package com.example.androidacademyhomework.database

import android.content.Context
import androidx.room.*
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MovieEntity::class,ActorsEntity::class], version = 1,exportSchema = false)
@TypeConverters(GenresConverter::class)
abstract class MovieDataBase : RoomDatabase() {
    abstract val moviesDao: MoviesDao
    abstract val actorsDao:ActorsDao
    companion object {
        fun create(applicationContext: Context) =
            Room.databaseBuilder(
                applicationContext,
                MovieDataBase::class.java,
                "Movies"
            ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
    }
}