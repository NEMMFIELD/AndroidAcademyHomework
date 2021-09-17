package com.example.androidacademyhomework.viewmodel


import android.content.Context
import androidx.lifecycle.*
import com.example.androidacademyhomework.data.JsonMovieRepository
import com.example.androidacademyhomework.data.MovieRepository
import com.example.androidacademyhomework.model.Model
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlin.coroutines.coroutineContext

class MovieViewModel(private val context: Context): ViewModel() {
    val repository: JsonMovieRepository = JsonMovieRepository(context)
    private val _loading = MutableLiveData<Boolean>(false)
    private val _moviesList = MutableLiveData<List<Model>>(emptyList())

    val loading:LiveData<Boolean> get() = _loading
    val moviesList:LiveData<List<Model>> get() = _moviesList

    init{
        fetchMoviesList()
    }

    @ExperimentalSerializationApi
    fun fetchMoviesList()
    {
        viewModelScope.launch {
            _loading.value = true
        val cinemas =repository.loadMovies()
        val updatedCinemas= _moviesList.value?.plus(cinemas).orEmpty()
            _moviesList.value = updatedCinemas
            _loading.value = false
        }
    }
}
class MovieViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}