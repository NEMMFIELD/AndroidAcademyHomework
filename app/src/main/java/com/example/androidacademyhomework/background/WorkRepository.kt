package com.example.androidacademyhomework.background


import android.annotation.SuppressLint
import androidx.work.*
import com.example.androidacademyhomework.Utils.Companion.WORKER_DELAY_TIME
import kotlinx.serialization.ExperimentalSerializationApi
import java.util.concurrent.TimeUnit

class WorkRepository {
    private val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.UNMETERED)
        .setRequiresCharging(false).build()
    @SuppressLint("InvalidPeriodicWorkRequestInterval")
    @ExperimentalSerializationApi
    val periodicWork = PeriodicWorkRequest.Builder(
        UploadMovieWorker::class.java,
       15L,
        TimeUnit.MINUTES)
        .setConstraints(constraints)
        .setInitialDelay(WORKER_DELAY_TIME, TimeUnit.SECONDS)
        .build()
}