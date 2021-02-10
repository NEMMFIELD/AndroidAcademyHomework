package com.example.androidacademyhomework.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DbViewModel(application: Application):AndroidViewModel(application) {
    private val readAllData: LiveData<List<MovieDb>>
    private val repository:MovieRepository
    init {
        val movieDao = AppDatabase.getAppDataBase(application)?.movieDao()
        repository = MovieRepository(movieDao)
        readAllData = repository.readAllData
    }
    fun addMovie(movie:MovieDb)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMovie(movie)
        }
    }
}