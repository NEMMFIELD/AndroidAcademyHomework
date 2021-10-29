package com.example.androidacademyhomework.background


import androidx.work.*
import com.example.androidacademyhomework.Utils.Companion.WORKER_DELAY_TIME
import com.example.androidacademyhomework.Utils.Companion.WORKER_REPEAT_TIME
import kotlinx.serialization.ExperimentalSerializationApi
import java.util.concurrent.TimeUnit

class WorkRepository {
    private val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.UNMETERED)
        .setRequiresCharging(true).build()
    @ExperimentalSerializationApi
    val periodicWork = PeriodicWorkRequest.Builder(
        UploadMovieWorker::class.java,
        WORKER_REPEAT_TIME,
        TimeUnit.MINUTES
    ).setConstraints(constraints).setInitialDelay(WORKER_DELAY_TIME, TimeUnit.SECONDS).build()
}