package com.example.androidacademyhomework.network

import android.content.Context
import com.example.androidacademyhomework.model.Actor
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
        return RetrofitModule.moviesApi.getNowPlaying().results!!
    }

    suspend fun convertToModel(film: ResultsItem): Model {
        val movieInfo = film.id?.let { RetrofitModule.moviesApi.getMoviesInfo(it) }
        val actors = film.id?.let { RetrofitModule.moviesApi.getCast(it) }
        return Model(
            id = film.id,
            pgAge = film.adult!!,
            title = film.title,
            genres = listOf(movieInfo?.genres?.map{it!!.name}!!.joinToString()),
            runningTime = movieInfo.runtime!!,
            reviewCount = film.voteCount!!,
            isLiked = false,
            rating = film.voteAverage!!.toFloat(),
            imageUrl = film.posterPath,
            detailImageUrl = film.backdropPath!!,
            storyLine = film.overview!!,
            actors = actors?.cast!! as List<CastItem>
        )
    }

   /* suspend fun convertToActor(actors: List<CastItem?>?, film:ResultsItem): Actor {

        val actors = film.id?.let { RetrofitModule.moviesApi.getCast(it) }
        return Actor(
            name = actors?.cast?.map{it!!.name},
            imageActor = actors?.cast?.map{it?.profilePath}
        )
    }*/

    suspend fun parseMovie(list: List<ResultsItem>): List<Model> {
        var listMovies: MutableList<Model> = mutableListOf()
        for (i in list.indices) {
            listMovies.add(convertToModel(list[i]))

        }
        return listMovies
    }

   /* suspend fun convertGenres(): List<Genre> {
        val listGenres: MutableList<Genre> = mutableListOf()
        val generalResponse = loadMoviesNet()

            val movieInfo = RetrofitModule.moviesApi.getMoviesInfo(generalResponse[i]?.id!!)
            val newGenre = Genre(
                generalResponse[0]?.id!!,
                movieInfo.genres?.get(0)?.name!!
            )
            listGenres.add(newGenre)

        return listGenres
    }*/
}