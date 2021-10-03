package com.example.androidacademyhomework.viewmodel

import androidx.lifecycle.*
import com.example.androidacademyhomework.database.ActorsEntity
import com.example.androidacademyhomework.network.MovieRepo
import kotlinx.serialization.ExperimentalSerializationApi
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ActorsViewModel(private val repository: MovieRepo):ViewModel() {
    //val allActors: LiveData<List<ActorsEntity>> = repository.allActors.asLiveData()

    @ExperimentalSerializationApi
    fun insertActor(movieId:Long) = viewModelScope.launch {
        repository.insertActorsToDb(movieId)
    }
}

class ActorsViewModelFactory(private val repository: MovieRepo) : ViewModelProvider.Factory {
    @ExperimentalSerializationApi
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActorsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ActorsViewModel(repository = repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}