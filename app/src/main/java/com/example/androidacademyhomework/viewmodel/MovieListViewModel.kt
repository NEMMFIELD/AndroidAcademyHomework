package com.example.androidacademyhomework.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.androidacademyhomework.data.model.viewholder.MoviePagingSource
import com.example.androidacademyhomework.data.model.viewholder.ResultsItem
import com.example.androidacademyhomework.network.MoviesApi
import kotlinx.coroutines.flow.Flow

class MovieListViewModel(private val movieApi: MoviesApi) : ViewModel() {
    val listData: Flow<PagingData<ResultsItem>> = Pager(PagingConfig(pageSize = 20))
    {
        MoviePagingSource(movieApi)
    }.flow.cachedIn(viewModelScope)
}

@Suppress("UNCHECKED_CAST")
class MovieListViewModelFactory(private val movieApi: MoviesApi) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        MovieListViewModel::class.java -> MovieListViewModel(movieApi)
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T
}