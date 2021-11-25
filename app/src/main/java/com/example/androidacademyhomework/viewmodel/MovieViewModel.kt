package com.example.androidacademyhomework.viewmodel

import android.net.ConnectivityManager
import android.net.NetworkInfo
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

@ExperimentalSerializationApi
class MovieViewModel(private val repository: MovieRepo) : ViewModel() {
    val allMovies: LiveData<List<MovieEntity>> = repository.allMovies.asLiveData()
    private val _mutableActorList = MutableLiveData<List<ActorsEntity>>(emptyList())
    val actorList: LiveData<List<ActorsEntity>?> get() = _mutableActorList

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
