package com.example.androidacademyhomework.database

import androidx.lifecycle.LiveData

class MovieRepository(private val movieDao: MovieDao?) {
    val readAllData:LiveData<List<MovieDb>> = movieDao!!.getAll()

    fun addMovie(movie:MovieDb){
        movieDao?.addMovie(movie)
    }
}