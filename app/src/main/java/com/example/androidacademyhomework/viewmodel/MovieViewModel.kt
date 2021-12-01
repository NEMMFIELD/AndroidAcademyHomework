package com.example.androidacademyhomework.viewmodel

import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.provider.CalendarContract
import androidx.lifecycle.*
import com.example.androidacademyhomework.Utils.Companion.page
import com.example.androidacademyhomework.database.MovieEntity
import com.example.androidacademyhomework.network.MovieRepo
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.androidacademyhomework.database.ActorsEntity
import kotlinx.coroutines.Dispatchers
import java.util.*

@ExperimentalSerializationApi
class MovieViewModel(private val repository: MovieRepo) : ViewModel() {
    val allMovies: LiveData<List<MovieEntity>> = repository.allMovies.asLiveData()
    private val _mutableActorList = MutableLiveData<List<ActorsEntity>>(emptyList())
    val actorList: LiveData<List<ActorsEntity>?> get() = _mutableActorList
    private val _calendarIntent = MutableLiveData<Intent?>()
    val calendarIntent get() = _calendarIntent

    fun insert() = viewModelScope.launch {
        try {
            repository.addNewAndGetUpdated()
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


    fun loadMore() {
        viewModelScope.launch {
            page++
            insert()
        }
    }

    fun updateLike(movie: MovieEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateMovieLike(movie)
        }
    }
    fun scheduleMovieInCalendar(movieTitle: String, dateAndTime: Calendar) {
        val intent = Intent(Intent.ACTION_INSERT)
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
        _calendarIntent.value = intent
    }

    fun scheduleMovieDone()
    {
        _calendarIntent.value = null
    }
}

class MovieViewModelFactory(private val repository: MovieRepo) : ViewModelProvider.Factory {
    @ExperimentalSerializationApi
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieViewModel(repository = repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
