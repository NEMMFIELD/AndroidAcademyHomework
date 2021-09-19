package com.example.androidacademyhomework.network

import android.content.Context
import com.example.androidacademyhomework.model.Model
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

interface MovieRepository
{
    suspend fun loadMoviesNet() : List<ResultsItem?>?
}
class MovieRepo(private val context:Context):MovieRepository
{
    @ExperimentalSerializationApi
    override suspend fun loadMoviesNet(): List<ResultsItem?> {
        return RetrofitModule.moviesApi.getNowPlaying().results!!
    }
   suspend fun convertToModel(film:ResultsItem):Model
    {
            return Model(
                id=film.id,
                16,
                title = film.title,
                null,
                runningTime = 0,
                reviewCount = 0,
                isLiked = false,
                rating = 0,
                imageUrl = film.posterPath,
                detailImageUrl = "",
                storyLine = "",
                actors = emptyList()
            )
    }
   suspend fun parseMovie(list:List<ResultsItem>):List<Model>
    {
        var listMovies:MutableList<Model> = mutableListOf()
        for (i in list.indices)
        {
            listMovies.add(convertToModel(list[i]))

        }
        return listMovies
    }
}