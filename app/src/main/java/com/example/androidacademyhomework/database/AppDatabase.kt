package com.example.androidacademyhomework.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.androidacademyhomework.data.model.viewholder.ResultsItem

@Database(entities = [ResultsItem::class],version = 1,exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        var INSTANCE: AppDatabase? = null
        fun getAppDataBase(context: Context): AppDatabase? {
            if (INSTANCE == null){
                synchronized(AppDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "myDB").build()
                }
            }
            return INSTANCE
        }
        fun destroyDataBase(){
            INSTANCE = null
        }
    }
}