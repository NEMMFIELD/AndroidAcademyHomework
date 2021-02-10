package com.example.androidacademyhomework.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MovieDb::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
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

  /*  private fun readFilmsFromDb(): LiveData<List<MovieDb>>? {
        val db = AppDatabase.INSTANCE
        return db?.movieDao()?.getAll()
    }

    private fun saveFilmsToDb(films: List<MovieDb>) {
        val db = AppDatabase.INSTANCE
        db?.movieDao()?.insertAll(films)
    }*/
}