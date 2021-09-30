package com.example.androidacademyhomework.viewmodel

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

@ExperimentalSerializationApi
class MovieViewModel(private val repository: MovieRepo) : ViewModel() {
   // private val _moviesList = MutableLiveData<List<MovieEntity>>(emptyList())
    //val moviesList: LiveData<List<MovieEntity>> get() = _moviesList.asFlow().asLiveData()
val allMovies:LiveData<List<MovieEntity>> = repository.allMovies.asLiveData()

    fun insert() = viewModelScope.launch {
        repository.addNewAndGetUpdated()
    }

    fun loadMore() {
        viewModelScope.launch {
            page++
            //_moviesList.value = repository.addNewAndGetUpdated()
            insert()
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