package com.example.androidacademyhomework.notifications

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import androidx.annotation.MainThread
import androidx.annotation.RequiresApi
import androidx.annotation.WorkerThread
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_HIGH
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.net.toUri
import com.example.androidacademyhomework.MainActivity
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.database.MovieEntity
import com.example.androidacademyhomework.model.Model
import okhttp3.internal.notify

interface Notifications {
    fun initialize()
    fun showNotification(movie:MovieEntity)
    fun dismissNotification(movieId: Long)
}

class MoviesNotification(private val context: Context) : Notifications {
    companion object {
        private const val CHANNEL_NEW_MOVIES = "new_movies"
        private const val REQUEST_CONTENT = 1
        private const val MOVIE_TAG = "movie"
    }

    private val notificationManagerCompat: NotificationManagerCompat =
        NotificationManagerCompat.from(context)


    @RequiresApi(Build.VERSION_CODES.O)
    override fun initialize() {
            if (notificationManagerCompat.getNotificationChannel(CHANNEL_NEW_MOVIES) == null)
            {
                notificationManagerCompat.createNotificationChannel(
                    NotificationChannelCompat.Builder(CHANNEL_NEW_MOVIES,
                        IMPORTANCE_HIGH)
                        .setName("New Movies")
                        .setDescription("All new added movies")
                        .build()
                )
            }
    }

    @WorkerThread
    override fun showNotification(movie:MovieEntity) {
        val contentUri = "com.example.androidacademyhomework/${movie.id}".toUri()
        val builder  = NotificationCompat.Builder(context, CHANNEL_NEW_MOVIES)
            .setContentTitle("New added movie")
            .setContentText(movie.title +" with "+ movie.rating + " rating")
            .setSmallIcon(R.drawable.ic_star_icon)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOnlyAlertOnce(true)
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    REQUEST_CONTENT,
                    Intent(context,MainActivity::class.java)
                        .setAction(Intent.ACTION_VIEW)
                        .setData(contentUri),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
        movie.id?.toInt()?.let { notificationManagerCompat.notify(TAG, it,builder.build()) }
    }
    @MainThread
    override fun dismissNotification(movieId:Long) {
        notificationManagerCompat.cancel(MOVIE_TAG,movieId.toInt())
    }

}