package com.example.androidacademyhomework.viewmodel

import androidx.lifecycle.*
import com.example.androidacademyhomework.database.ActorsEntity
import com.example.androidacademyhomework.database.MovieEntity
import com.example.androidacademyhomework.network.MovieRepo
import kotlinx.serialization.ExperimentalSerializationApi

class FavouriteViewModel(private val repository:MovieRepo):ViewModel()
{
    private val _mutableFavouriteMovies = MutableLiveData<List<MovieEntity>>(emptyList())
    val favouriteMovies: LiveData<List<MovieEntity>?> get() = _mutableFavouriteMovies
    init {
        _mutableFavouriteMovies.value = getMoviesFav(true)
    }

    fun getMoviesFav(liked:Boolean): List<MovieEntity> {
      return  repository.getFavouriteMovies(liked)
    }

}
@ExperimentalSerializationApi
class FavouriteMoviesViewModelFactory(private val repository: MovieRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavouriteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavouriteViewModel(repository = repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}