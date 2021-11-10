package com.example.androidacademyhomework.background

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.asLiveData
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.androidacademyhomework.database.MovieEntity
import com.example.androidacademyhomework.network.MovieRepo
import com.example.androidacademyhomework.notifications.MoviesNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.withContext
import java.util.*


class UploadMovieWorker(private val context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {

    @Suppress("EXPERIMENTAL_API_USAGE")
    override suspend fun doWork(): Result = withContext(Dispatchers.IO)
    {
        Log.d("uploadWork", "It is working")
       return@withContext  runCatching {
           val repository = MovieRepo(context = applicationContext)
          // val movies = repository.addNewAndGetUpdated() as? List<MovieEntity>
           repository.addNewAndGetUpdated()
            Log.d("work", "doWork: Success function called")
            val dateNow = Calendar.getInstance().time
            println("Work Manager works! $dateNow")
           val movies = repository.getMovies()
           checkNewMovies(movies = movies)
            Result.success()
        }.getOrElse {
           Result.failure() }
    }

    private fun checkNewMovies(movies: List<MovieEntity>) {
        val movie = movies.maxByOrNull { it.rating }
            sayNotification(movie!!)
    }

    private fun sayNotification(movie: MovieEntity) {
        val notifications = MoviesNotification(context = context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notifications.initialize()
        }
        notifications.showNotification(movie)
    }
}