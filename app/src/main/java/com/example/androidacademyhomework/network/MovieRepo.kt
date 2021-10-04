package com.example.androidacademyhomework.network

import android.content.Context
import androidx.lifecycle.asLiveData
import com.example.androidacademyhomework.Utils.Companion.page
import com.example.androidacademyhomework.database.ActorsEntity
import com.example.androidacademyhomework.database.MovieDataBase
import com.example.androidacademyhomework.database.MovieEntity
import com.example.androidacademyhomework.model.Model
import com.example.androidacademyhomework.network.pojopack.ActorsResponse
import com.example.androidacademyhomework.network.pojopack.CastItem
import com.example.androidacademyhomework.network.pojopack.ResultsItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.ExperimentalSerializationApi

interface MovieRepository {
    suspend fun loadMoviesNet(): List<ResultsItem?>?
    suspend fun addNewAndGetUpdated()
   // fun getActors(id:Long): List<ActorsEntity>
    suspend fun insertActorsToDb(movieId: Long)
}

class MovieRepo(context: Context) : MovieRepository {
    private val db: MovieDataBase = MovieDataBase.create(context)
    val allMovies: Flow<List<MovieEntity>> = db.moviesDao.getAllMovies()
    val allActors: Flow<List<ActorsEntity>> = db.actorsDao.getAllActors()
    @ExperimentalSerializationApi
    override suspend fun loadMoviesNet(): List<ResultsItem?> {
        return RetrofitModule.moviesApi.getNowPlaying(page).results!!
    }

    override suspend fun addNewAndGetUpdated() {
        val list = parseMovie(loadMoviesNet() as List<ResultsItem>)
        val newList = mutableListOf<MovieEntity>()
        for (i in list.indices) {
            convertToMovieEntity(list[i]).let { newList.add(it) }
        }
        db.moviesDao.insertMovie(newList)
    }

   /* override fun getActors(id:Long): List<ActorsEntity> {
        return db.actorsDao.getAllActors(id)
    }*/

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

    @ExperimentalSerializationApi
    override suspend fun insertActorsToDb(movieId: Long) {
        val newList = mutableListOf<ActorsEntity>()
        val actors: List<CastItem>? =
            RetrofitModule.moviesApi.getCast(movieId).cast as List<CastItem>?
        for (i in actors!!.indices) {
            val convertedActors = convertToActorsEntity(actors[i], movieId)
            newList.add(convertedActors)
        }
        db.actorsDao.insertActors(newList)
        //db.actorsDao.getAllActors(movieId)
    }

    //Конвертирование в сущность БД "Актеры"
    fun convertToActorsEntity(actor: CastItem, id: Long): ActorsEntity {
        return ActorsEntity(
            id = actor.id!!.toLong(),
            name = actor.name,
            filmId = id,
            profilePath = actor.profilePath
        )
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
        // actors = movie.actors as List<ActorsEntity>
    )

    suspend fun parseMovie(list: List<ResultsItem>): List<Model> {
        val listMovies: MutableList<Model> = mutableListOf()
        for (i in list.indices) {
            convertToModel(list[i])?.let { listMovies.add(it) }
        }
        return listMovies
    }
}