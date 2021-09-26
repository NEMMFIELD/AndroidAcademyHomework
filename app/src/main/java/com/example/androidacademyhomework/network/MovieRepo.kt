package com.example.androidacademyhomework.network

import android.content.Context
import com.example.androidacademyhomework.Utils.Companion.page
import com.example.androidacademyhomework.database.MovieDataBase
import com.example.androidacademyhomework.database.MovieEntity
import com.example.androidacademyhomework.model.Model
import com.example.androidacademyhomework.network.pojopack.CastItem
import com.example.androidacademyhomework.network.pojopack.ResultsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi

interface MovieRepository {
    suspend fun loadMoviesNet(): List<ResultsItem?>?
    suspend fun getAllMovies(): List<MovieEntity>
    suspend fun addNewAndGetUpdated(): List<MovieEntity>
}

class MovieRepo(context: Context) : MovieRepository {
    private val db: MovieDataBase = MovieDataBase.create(context)

    @ExperimentalSerializationApi
    override suspend fun loadMoviesNet(): List<ResultsItem?> {
        return RetrofitModule.moviesApi.getNowPlaying(page).results!!
    }

    override suspend fun getAllMovies(): List<MovieEntity> = db.moviesDao.getAllMovies()

    @ExperimentalSerializationApi
    override suspend fun addNewAndGetUpdated(): List<MovieEntity> = withContext(Dispatchers.IO)
    {
        val list = parseMovie(loadMoviesNet() as List<ResultsItem>)
        val newList = mutableListOf<MovieEntity>()
        for (i in list.indices) {
            convertToMovieEntity(list[i]).let { newList.add(it) }
        }
        if (newList.isEmpty()) db.moviesDao.insertMovie(newList)
        getAllMovies()
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

    fun convertToMovieEntity(movie: Model): MovieEntity = MovieEntity(
        id = movie.id,
        pgAge = movie.pgAge,
        title = movie.title,
        genres = movie.genres,
        runningTime = movie.runningTime,
        reviewCount = movie.reviewCount,
        isLiked = movie.isLiked,
        rating = movie.rating,
        imageUrl = movie.imageUrl,
        detailImageUrl = movie.detailImageUrl,
        storyLine = movie.storyLine
    )

    suspend fun parseMovie(list: List<ResultsItem>): List<Model> {
        val listMovies: MutableList<Model> = mutableListOf()
        for (i in list.indices) {
            convertToModel(list[i])?.let { listMovies.add(it) }
        }
        return listMovies
    }
}