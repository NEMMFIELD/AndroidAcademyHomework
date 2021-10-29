package com.example.androidacademyhomework.background

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.lifecycle.asLiveData
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.androidacademyhomework.database.MovieDataBase
import com.example.androidacademyhomework.database.MovieEntity
import com.example.androidacademyhomework.model.Model
import com.example.androidacademyhomework.network.MovieRepo
import com.example.androidacademyhomework.notifications.MoviesNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi


class UploadMovieWorker(private val context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val repository = MovieRepo(context = applicationContext)
                val oldMovies = repository.allMovies.asLiveData().value
                repository.allMovies.asLiveData().value?.let { writeToDb(movies = it) }
                val newMovies = repository.allMovies.asLiveData().value
                //repository.addNewAndGetUpdated() 2 вариант
                Log.d("do work success", "doWork: Success function called")
                if (!oldMovies.isNullOrEmpty() || !newMovies.isNullOrEmpty()) {
                    oldMovies?.let {
                        newMovies?.let { it1 ->
                            checkNewMovies(
                                oldMovies = it,
                                newMovies = it1
                            )
                        }
                    }
                }
                Result.success()
            } catch (e: Exception) {
                Result.failure()
            }
        }
    }

    private suspend fun writeToDb(movies: List<MovieEntity>) {
        val movieDao = MovieDataBase.create(applicationContext).moviesDao
        movieDao.insertMovie(movies)
    }

    private fun checkNewMovies(oldMovies: List<MovieEntity>, newMovies: List<MovieEntity>) {
        val movie = newMovies.subtract(oldMovies).maxByOrNull { it.rating }
            ?: oldMovies.maxByOrNull { it.rating }
        if (movie != null) {
            sayNotification(movie)
        }
    }

    private fun sayNotification(movie: MovieEntity) {
        val notifications = MoviesNotification(context = context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notifications.initialize()
        }
        notifications.showNotification(movie)
    }
}