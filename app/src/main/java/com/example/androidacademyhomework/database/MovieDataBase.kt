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
        @Volatile
        private var INSTANCE: MovieDataBase? = null
        fun create(context: Context): MovieDataBase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDataBase::class.java,
                    "movies_database"
                ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
       /* fun create(applicationContext: Context) =
            Room.databaseBuilder(
                applicationContext,
                MovieDataBase::class.java,
                "Movies"
            ).allowMainThreadQueries().fallbackToDestructiveMigration().build()*/
    }
}