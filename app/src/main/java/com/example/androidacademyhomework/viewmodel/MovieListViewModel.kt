package com.example.androidacademyhomework.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidacademyhomework.data.model.viewholder.Movie
import com.example.androidacademyhomework.data.model.viewholder.ResultsItem
import com.example.androidacademyhomework.network.API_KEY
import com.example.androidacademyhomework.network.LANGUAGE
import com.example.androidacademyhomework.network.PAGE_NUMB
import com.example.androidacademyhomework.network.RetrofitModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieListViewModel(private val context: Context) : ViewModel() {
    private var scope = CoroutineScope(Dispatchers.Main)
    private val _mutableMovieList = MutableLiveData<List<ResultsItem>>(emptyList())
    val movieList: LiveData<List<ResultsItem>> get() = _mutableMovieList
    // private val _mutableActorList = MutableLiveData<List<Actor>>(emptyList())
    //val actorList: LiveData<List<Actor>> get() = _mutableActorList

    fun loadData() {
        scope.launch {
            loadMoviesList()
        }
    }

    private suspend fun loadMoviesList() {
        val updatedMovieList = _mutableMovieList.value?.plus(RetrofitModule.moviesApi.getNowPlaying(
            API_KEY, LANGUAGE,
            PAGE_NUMB
        )).orEmpty()
        _mutableMovieList.value = updatedMovieList as List<ResultsItem>
    }

    fun loadActors(pos: Int) {
        scope.launch { reloadActors(pos) }
    }

    private fun reloadActors(pos: Int) {
        // _mutableActorList.value = loadMovies(context)[pos].actors
    }
}

@Suppress("UNCHECKED_CAST")
class MovieListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        MovieListViewModel::class.java -> MovieListViewModel(context)
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T
}