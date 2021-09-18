package com.example.androidacademyhomework.viewmodel


import androidx.lifecycle.*
import com.example.androidacademyhomework.data.MovieRepository
import com.example.androidacademyhomework.model.Model
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {
    //val repository: JsonMovieRepository = JsonMovieRepository(context)
    // private val _loading = MutableLiveData<Boolean>(false)
    private val _moviesList = MutableLiveData<List<Model>>(emptyList())

    //val loading:LiveData<Boolean> get() = _loading
    val moviesList: LiveData<List<Model>> get() = _moviesList

    init {
        fetchMoviesList()
    }

    @ExperimentalSerializationApi
    fun fetchMoviesList() {
        viewModelScope.launch {
            //  _loading.value = true
            // val cinemas =repository.loadMovies()
            //val updatedCinemas= _moviesList.value?.plus(cinemas).orEmpty()
            // _moviesList.value = updatedCinemas
            _moviesList.value = repository.loadMovies()
            //   _loading.value = false
        }
    }
}

class MovieViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieViewModel(repository = repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}