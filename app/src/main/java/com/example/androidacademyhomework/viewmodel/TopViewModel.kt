package com.example.androidacademyhomework.viewmodel

import android.content.*
import android.net.Uri
import android.provider.CalendarContract
import androidx.lifecycle.*
import com.example.androidacademyhomework.Utils
import com.example.androidacademyhomework.database.ActorsEntity
import com.example.androidacademyhomework.database.MovieEntity
import com.example.androidacademyhomework.network.MovieRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import java.util.*
import kotlin.time.ExperimentalTime

@ExperimentalSerializationApi
class TopViewModel(private val repository: MovieRepo): ViewModel() {
    val topAllMovies: LiveData<List<MovieEntity>> = repository.allTopMovies.asLiveData()
    private val _mutableActorList = MutableLiveData<List<ActorsEntity>>(emptyList())
    val actorList: LiveData<List<ActorsEntity>?> get() = _mutableActorList

    fun insert(path: String, type: String) = viewModelScope.launch {
        try {
            repository.addNewAndGetUpdated(path, type)
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


    fun loadMore(path: String, type: String) {
        viewModelScope.launch {
            Utils.page++
            insert(path = path, type)
        }
    }

    fun updateLike(movie: MovieEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateMovieLike(movie)
        }
    }
}
@ExperimentalSerializationApi
class TopViewModelFactory(private val repository: MovieRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TopViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TopViewModel(repository = repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}