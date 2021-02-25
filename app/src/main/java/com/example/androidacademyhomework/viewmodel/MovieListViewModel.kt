package com.example.androidacademyhomework.viewmodel

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.androidacademyhomework.data.model.viewholder.CastItem
import com.example.androidacademyhomework.data.model.viewholder.MoviePagingSource
import com.example.androidacademyhomework.data.model.viewholder.ResultsItem
import com.example.androidacademyhomework.network.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MovieListViewModel(private val movieApi:MoviesApi) : ViewModel() {
    private var scope = CoroutineScope(Dispatchers.Main)
    val listData: Flow<PagingData<ResultsItem>> = Pager(PagingConfig(pageSize = 20))
    {
        MoviePagingSource(movieApi)
    }.flow.cachedIn(viewModelScope)
  /*
    private val _mutableMovieList = MutableLiveData<List<ResultsItem>>(emptyList())
    val movieList: LiveData<List<ResultsItem>> get() = _mutableMovieList
    private val _mutableActorList = MutableLiveData<List<CastItem?>?>(emptyList())
    val actorList: LiveData<List<CastItem?>?> get() = _mutableActorList*/

   /* init {
        loadData()
    }

    fun loadData() {
        scope.launch {
            loadMoviesList()
        }
    }

    private suspend fun loadMoviesList() {
        val updatedMovieList = _mutableMovieList.value?.plus(
            RetrofitModule.moviesApi.getNowPlaying(
                API_KEY, LANGUAGE,
                PAGE_NUMB
            ).results as List<ResultsItem>
        ).orEmpty()
        _mutableMovieList.value = updatedMovieList
    }

    fun loadActors(pos: Int) {
        scope.launch { reloadActors(pos) }
    }

    private suspend fun reloadActors(pos: Int) {
        val movie = RetrofitModule.moviesApi.getNowPlaying(API_KEY, LANGUAGE, PAGE_NUMB)
        val movieId: Long = movie.results?.get(pos)?.id ?: 0
        val updatedActorList: List<CastItem?>? = _mutableActorList.value?.plus(
            RetrofitModule.moviesApi.getCast(
                movieId,
                API_KEY, LANGUAGE
            ).cast as List<CastItem?>
        )
        _mutableActorList.value = updatedActorList
    }*/
}

@Suppress("UNCHECKED_CAST")
class MovieListViewModelFactory(private val movieApi:MoviesApi) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        MovieListViewModel::class.java -> MovieListViewModel(movieApi)
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T
}