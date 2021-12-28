package com.example.androidacademyhomework.notifications

import android.app.Notification
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.MainThread
import androidx.annotation.RequiresApi
import androidx.annotation.WorkerThread
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_HIGH
import androidx.core.net.toUri
import com.example.androidacademyhomework.ui.MainActivity
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.Utils.Companion.BASE_IMAGE_URL
import com.example.androidacademyhomework.database.MovieEntity
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

interface Notifications {
    fun initialize()
    fun showNotification(movie: MovieEntity)
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
        if (notificationManagerCompat.getNotificationChannel(CHANNEL_NEW_MOVIES) == null) {
            notificationManagerCompat.createNotificationChannel(
                NotificationChannelCompat.Builder(
                    CHANNEL_NEW_MOVIES,
                    IMPORTANCE_HIGH
                )
                    .setName("New Movies")
                    .setDescription("All new added movies")
                    .build()
            )
        }
    }

    @WorkerThread
    override fun showNotification(movie: MovieEntity) {
        val bitmap = getBitmapFromURL(movie)
        val contentUri = "com.example.androidacademyhomework/${movie.id}".toUri()
        val builder = NotificationCompat.Builder(context, CHANNEL_NEW_MOVIES)
            .setContentTitle("New added movie")
            .setContentText(movie.title + " with " + movie.rating + " rating")
            .setSmallIcon(R.drawable.ic_notification_movie)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setDefaults(Notification.DEFAULT_ALL)
            .setOnlyAlertOnce(true)
           // .setAutoCancel(true)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    REQUEST_CONTENT,
                    Intent(context, MainActivity::class.java)
                        .setAction(Intent.ACTION_VIEW)
                        .setData(contentUri),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
        movie.id?.toInt()?.let { notificationManagerCompat.notify(MOVIE_TAG, it, builder.build()) }
    }

    private fun getBitmapFromURL(movie: MovieEntity): Bitmap? {
        return try {
            val url = URL(BASE_IMAGE_URL + movie.imageUrl)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.getInputStream()
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    @MainThread
    override fun dismissNotification(movieId: Long) {
        notificationManagerCompat.cancel(MOVIE_TAG, movieId.toInt())
    }
}