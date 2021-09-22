package com.example.androidacademyhomework.network

import android.content.Context
import com.example.androidacademyhomework.Utils.Companion.page
import com.example.androidacademyhomework.model.Model
import com.example.androidacademyhomework.network.pojopack.CastItem
import com.example.androidacademyhomework.network.pojopack.ResultsItem
import kotlinx.serialization.ExperimentalSerializationApi

interface MovieRepository {
    suspend fun loadMoviesNet(): List<ResultsItem?>?
}

class MovieRepo(private val context: Context) : MovieRepository {
    @ExperimentalSerializationApi
    override suspend fun loadMoviesNet(): List<ResultsItem?> {
        return RetrofitModule.moviesApi.getNowPlaying(page).results!!
    }

    suspend fun convertToModel(film: ResultsItem): Model? {
        val movieInfo = film.id?.let { RetrofitModule.moviesApi.getMoviesInfo(it) }
        val actors = film.id?.let { RetrofitModule.moviesApi.getCast(it) }
        return film.backdropPath?.let {
            Model(
                id = film.id,
                pgAge = film.adult!!,
                title = film.title,
                genres = listOf(movieInfo?.genres?.map { it!!.name }!!.joinToString()),
                runningTime = movieInfo.runtime!!,
                reviewCount = film.voteCount!!,
                isLiked = false,
                rating = film.voteAverage!!.toFloat(),
                imageUrl = film.posterPath,
                detailImageUrl = it,
                storyLine = film.overview!!,
                actors = actors?.cast!! as List<CastItem>
            )
        }
    }

    suspend fun parseMovie(list: List<ResultsItem>): List<Model> {
        val listMovies: MutableList<Model> = mutableListOf()
        for (i in list.indices) {
            convertToModel(list[i])?.let { listMovies.add(it) }
        }
        return listMovies
    }
}