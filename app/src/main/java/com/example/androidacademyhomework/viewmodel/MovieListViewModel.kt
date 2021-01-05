package com.example.androidacademyhomework.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.example.androidacademyhomework.data.model.Movie
import com.example.androidacademyhomework.data.model.loadMovies
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieListViewModel(private val context: Context): ViewModel() {
  private var scope= CoroutineScope(Dispatchers.Main)
    private val _mutableMovieList=MutableLiveData<List<Movie>>(emptyList())
    val movieList:LiveData<List<Movie>>get() = _mutableMovieList

     fun loadData() {
         scope.launch { reloadMovies() }
     }

    private suspend fun reloadMovies()
    {
        _mutableMovieList.value= loadMovies(context)
    }

    }

@Suppress("UNCHECKED_CAST")
class MovieListViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        MovieListViewModel::class.java -> MovieListViewModel(context)
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T
}