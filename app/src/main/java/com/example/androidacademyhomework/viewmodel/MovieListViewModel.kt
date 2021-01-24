package com.example.androidacademyhomework.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidacademyhomework.data.model.Actor
import com.example.androidacademyhomework.data.model.Movie
import com.example.androidacademyhomework.data.model.loadMovies
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieListViewModel(private val context: Context) : ViewModel() {
    private var scope = CoroutineScope(Dispatchers.Main)
    private val _mutableMovieList = MutableLiveData<List<Movie>>(emptyList())
    val movieList: LiveData<List<Movie>> get() = _mutableMovieList
    private val _mutableActorList = MutableLiveData<List<Actor>>(emptyList())
    val actorList: LiveData<List<Actor>>
        get() = _mutableActorList

    fun loadData() {
        scope.launch {
            reloadMovies()
        }
    }

    private suspend fun reloadMovies() {
        _mutableMovieList.value = loadMovies(context)
    }

    fun loadActors(pos: Int) {
        scope.launch { reloadActors(pos) }
    }

    private suspend fun reloadActors(pos: Int) {
        _mutableActorList.value = loadMovies(context)[pos].actors
    }

}

@Suppress("UNCHECKED_CAST")
class MovieListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        MovieListViewModel::class.java -> MovieListViewModel(context)
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T
}