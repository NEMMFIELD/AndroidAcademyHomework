package com.example.androidacademyhomework.background

import android.content.Context
import android.util.Log
import androidx.lifecycle.asLiveData
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.androidacademyhomework.database.MovieDataBase
import com.example.androidacademyhomework.database.MovieEntity
import com.example.androidacademyhomework.network.MovieRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class UploadMovieWorker(context:Context, workerParameters: WorkerParameters): CoroutineWorker(context,workerParameters) {
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try{
                val repository = MovieRepo(context = applicationContext)
                repository.allMovies.asLiveData().value?.let { writeToDb(movies = it) }
                //repository.addNewAndGetUpdated() 2 вариант
                Log.d("do work success","doWork: Success function called")
                 Result.success()
            }
            catch (e:Exception)
            {
                Result.failure()
            }
        }
    }
    private suspend fun writeToDb(movies:List<MovieEntity>)
    {
        val movieDao = MovieDataBase.create(applicationContext).moviesDao
        movieDao.insertMovie(movies)
    }
}