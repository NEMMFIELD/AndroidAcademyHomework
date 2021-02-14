package com.example.androidacademyhomework.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.androidacademyhomework.database.actor.ActorDb
import com.example.androidacademyhomework.database.actor.ActorRepository
import com.example.androidacademyhomework.database.movie.MovieDb
import com.example.androidacademyhomework.database.movie.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DbViewModel(application: Application):AndroidViewModel(application) {
    private val readAllData: LiveData<List<MovieDb>>
    private val readAllActors:LiveData<List<ActorDb>>
    private val repository: MovieRepository
    private val actorRepository:ActorRepository
    init {
        val movieDao = AppDatabase.getAppDataBase(application)?.movieDao()
        repository = MovieRepository(movieDao)
        readAllData = repository.readAllData
        val actorDao=AppDatabase.getAppDataBase(application)?.actorDao()
        actorRepository = ActorRepository(actorDao)
        readAllActors = actorRepository.readAllActors
    }
    fun addMovie(movie: MovieDb)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMovie(movie)
        }
    }

    fun addActor(actor:ActorDb)
    {
        viewModelScope.launch (Dispatchers.IO) {
            actorRepository.addActor(actor)
        }
    }

    }
