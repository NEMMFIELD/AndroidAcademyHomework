package com.example.androidacademyhomework.database

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.androidacademyhomework.database.actor.ActorDao
import com.example.androidacademyhomework.database.actor.ActorDb
import com.example.androidacademyhomework.database.movie.GenresConverter
import com.example.androidacademyhomework.database.movie.MovieDao
import com.example.androidacademyhomework.database.movie.MovieDb

@Database(entities = [MovieDb::class,ActorDb::class], version = 1, exportSchema = false)
@TypeConverters(GenresConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun actorDao(): ActorDao

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getAppDataBase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext, AppDatabase::class.java, "myDB"
                    ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase() {
            INSTANCE = null
        }
    }

    private fun readFilmsFromDb(): LiveData<List<MovieDb>>? {
        val db = AppDatabase.INSTANCE
        return db?.movieDao()?.getAll()
    }

    private fun saveFilmsToDb(movies: List<MovieDb>) {
        val db = AppDatabase.INSTANCE
        db?.movieDao()?.insertAll(movies)
    }
}

