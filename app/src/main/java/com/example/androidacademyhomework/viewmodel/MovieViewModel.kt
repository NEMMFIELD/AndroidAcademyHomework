package com.example.androidacademyhomework.viewmodel

import android.content.*
import android.net.Uri
import android.provider.CalendarContract
import androidx.lifecycle.*
import com.example.androidacademyhomework.Utils.Companion.page
import com.example.androidacademyhomework.database.ActorsEntity
import com.example.androidacademyhomework.database.MovieEntity
import com.example.androidacademyhomework.network.MovieRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import java.util.*
import kotlin.time.ExperimentalTime


@ExperimentalSerializationApi
class MovieViewModel(private val repository: MovieRepo) : ViewModel() {
    val allMovies: LiveData<List<MovieEntity>> = repository.allMovies.asLiveData()
    private val _mutableActorList = MutableLiveData<List<ActorsEntity>>(emptyList())
    val actorList: LiveData<List<ActorsEntity>?> get() = _mutableActorList
    private val _calendarIntent = MutableLiveData<Intent?>()
    val calendarIntent get() = _calendarIntent

    fun insert(path:String,type:String) = viewModelScope.launch {
        try {
            repository.addNewAndGetUpdated(path,type)
        } catch (e: Exception) {
        }
    }

    fun insertActor(movieId: Long) = viewModelScope.launch {
        try { //_mutableActorList.value = repository.getActors(movieId)
            if (_mutableActorList.value.isNullOrEmpty()) {
                repository.insertActorsToDb(movieId)
            }

        } catch (e: Exception) {
            println(e)
        }
        _mutableActorList.value = repository.getActors(movieId)
    }


    fun loadMore(path:String,type:String) {
        viewModelScope.launch {
            page++
            insert(path =path,type )
        }
    }

    fun updateLike(movie: MovieEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateMovieLike(movie)
        }
    }

    @ExperimentalTime
    fun scheduleMovieInCalendar(movieTitle: String, dateAndTime: Calendar, context: Context,movie: MovieEntity) {
        //1 Вариант
        /*  val intent = Intent(Intent.ACTION_INSERT)
          with(intent)
          {
              type = "vnd.android.cursor.item/event"
              putExtra(CalendarContract.Events.TITLE, movieTitle)
              putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, dateAndTime.timeInMillis)
              putExtra(
                  CalendarContract.EXTRA_EVENT_END_TIME,
                  dateAndTime.timeInMillis + 60 * 60 * 1000
              )
              // putExtra(CalendarContract.Events.ALL_DAY, true)
              putExtra(CalendarContract.Events.STATUS, 1)
              putExtra(CalendarContract.Events.VISIBLE, 1)
              putExtra(CalendarContract.Events.HAS_ALARM, 1)
          }
          _calendarIntent.value = intent*/

        val cr: ContentResolver = context.contentResolver
        val calID: Long = 1
        val values = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, dateAndTime.timeInMillis)
           put(CalendarContract.Events.DTEND, dateAndTime.timeInMillis + ((movie.runningTime/60)*60*60*1000))
            put(CalendarContract.Events.TITLE, movieTitle)
            put(CalendarContract.Events.CALENDAR_ID, calID)
            put(CalendarContract.Events.EVENT_TIMEZONE, "Europe/Moscow")
        }
        val uri: Uri? = cr.insert(CalendarContract.Events.CONTENT_URI, values)
        val builder: Uri.Builder = CalendarContract.CONTENT_URI.buildUpon()
        builder.appendPath("time")
        ContentUris.appendId(builder, dateAndTime.timeInMillis)
        val intent = Intent(Intent.ACTION_VIEW).setData(builder.build())
        _calendarIntent.value = intent
    }

    fun scheduleMovieDone() {
        _calendarIntent.value = null
    }
}

@ExperimentalSerializationApi
class MovieViewModelFactory(private val repository: MovieRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieViewModel(repository = repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
