package com.example.androidacademyhomework.di

import android.content.Context
import androidx.work.WorkManager
import com.example.androidacademyhomework.database.MovieDataBase
import com.example.androidacademyhomework.network.MovieRepo
import com.example.androidacademyhomework.network.NetworkConnection
import com.example.androidacademyhomework.notifications.MoviesNotification


class AppModule(context: Context) {
    private val dataBase:MovieDataBase = MovieDataBase.create(context)
     val workManager:WorkManager = WorkManager.getInstance(context)
    val moviesRepository:MovieRepo = MovieRepo(context)
    val moviesNotification = MoviesNotification(context)
    val networkConnection = NetworkConnection(context)
}