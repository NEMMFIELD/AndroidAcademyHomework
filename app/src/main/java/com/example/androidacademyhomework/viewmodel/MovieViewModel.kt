package com.example.androidacademyhomework.viewmodel


import androidx.lifecycle.*
import com.example.androidacademyhomework.Utils.Companion.page
import com.example.androidacademyhomework.database.MovieEntity
import com.example.androidacademyhomework.network.MovieRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class MovieViewModel(private val repository: MovieRepo) : ViewModel() {
    //val repository: JsonMovieRepository = JsonMovieRepository(context)
    // private val _loading = MutableLiveData<Boolean>(false)
    private val _moviesList = MutableStateFlow<List<MovieEntity>>(emptyList())
    //val loading:LiveData<Boolean> get() = _loading
    val moviesList: StateFlow<List<MovieEntity>> get() = _moviesList
    init {
        //fetchMoviesList()
        viewModelScope.launch {
            if (repository.loadMoviesNet().isNotEmpty()) _moviesList.value = repository.getAllMovies().stateIn(viewModelScope).value
             fetchMoviesList()
        }
    }

  fun fetchMoviesList() {
        viewModelScope.launch {
           //_loading.value = true
          // val cinemas =repository.parseMovie(repository.loadMoviesNet() as List<ResultsItem>)
          // val updatedCinemas= _moviesList.value?.plus(cinemas).orEmpty()
          // _moviesList.value = updatedCinemas
           _moviesList.value = repository.addNewAndGetUpdated().stateIn(viewModelScope).value
           //_loading.value = false
        }
    }
    fun loadMore()
    {
        viewModelScope.launch {
            page++
            val newValue = repository.addNewAndGetUpdated()
            val updatedValue =_moviesList.value.plus(newValue).orEmpty()
            _moviesList.value = updatedValue as List<MovieEntity>
        }
    }
}

class MovieViewModelFactory(private val repository: MovieRepo) : ViewModelProvider.Factory {
    @ExperimentalSerializationApi
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieViewModel(repository = repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}