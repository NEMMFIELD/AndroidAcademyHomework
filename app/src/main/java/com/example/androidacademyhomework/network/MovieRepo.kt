package com.example.androidacademyhomework.network

import android.content.Context
import com.example.androidacademyhomework.model.Model
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

interface MovieRepository
{
    suspend fun loadMoviesNet() : List<ResultsItem?>?
}
class MovieRepo(private val context:Context):MovieRepository {
    @ExperimentalSerializationApi
    override suspend fun loadMoviesNet(): List<ResultsItem?>? {
        return RetrofitModule.moviesApi.getNowPlaying().results
    }
}