package com.example.androidacademyhomework.network

import android.content.Context
import com.example.androidacademyhomework.Utils.Companion.page
import com.example.androidacademyhomework.database.ActorsEntity
import com.example.androidacademyhomework.database.MovieDataBase
import com.example.androidacademyhomework.database.MovieEntity
import com.example.androidacademyhomework.model.Model
import com.example.androidacademyhomework.network.pojopack.CastItem
import com.example.androidacademyhomework.network.pojopack.ResultsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi

interface MovieRepository {
    suspend fun loadMoviesNet(path:String): List<ResultsItem?>?
     suspend fun addNewAndGetUpdated(path:String,type:String)
    fun getActors(movieId: Long): List<ActorsEntity>
    suspend fun insertActorsToDb(movieId: Long)
    fun getMovies(): List<MovieEntity>
    fun getMovieById(id: Long): MovieEntity
    suspend fun  updateMovieLike(movie: MovieEntity)
    fun getFavouriteMovies(isLiked:Boolean):List<MovieEntity>
}

@ExperimentalSerializationApi
class MovieRepo(context: Context) : MovieRepository {
    private val db: MovieDataBase = MovieDataBase.create(context)
    val allMovies: Flow<List<MovieEntity>> = db.moviesDao.getAllMovies("now_playing")
    val allPopularMovies: Flow<List<MovieEntity>> = db.moviesDao.getAllMovies("popular")
    val allTopMovies: Flow<List<MovieEntity>> = db.moviesDao.getAllMovies("top_rated")
    val allUpcomingMovies: Flow<List<MovieEntity>> = db.moviesDao.getAllMovies("upcoming")

    //Загружаем через Retrofit2 список фильмов.
   override suspend fun loadMoviesNet(path:String): List<ResultsItem?> = withContext(Dispatchers.IO) {
        RetrofitModule.moviesApi.getNowPlaying(path,page).results!!
    }

    override fun getMovies() = db.moviesDao.getMovies()

    override fun getMovieById(id: Long): MovieEntity = db.moviesDao.getMoviebyId(id)

    override suspend fun updateMovieLike(movie: MovieEntity) = if (movie.isLiked)
    {
        db.moviesDao.updateLike(movie)
    }
    else
    {
        db.moviesDao.updateLike(movie)
    }

    override fun getFavouriteMovies(isLiked: Boolean): List<MovieEntity>
    {
        if (isLiked)
        {
             return db.moviesDao.getFavouritesMovies(isLiked)
        }
        return emptyList()
    }




    //Конвертируем ResultsItem в Model
    @ExperimentalSerializationApi
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

    //Конвертирование в сущность БД "Актеры"
    fun convertToActorsEntity(actor: CastItem, id: Long): ActorsEntity {
        return ActorsEntity(
            id = actor.id!!.toLong(),
            name = actor.name,
            filmId = id,
            profilePath = actor.profilePath
        )
    }

    //Конвертирование Model в MovieEntity, для БД.
    fun convertToMovieEntity(movie: Model,type:String): MovieEntity = MovieEntity(
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
        storyLine = movie.storyLine,
        listType = type
        // actors = movie.actors as List<ActorsEntity>
    )

    //from List to List<Model>
    suspend fun parseMovie(list: List<ResultsItem>): List<Model> {
        val listMovies: MutableList<Model> = mutableListOf()
        for (i in list.indices) {
            convertToModel(list[i])?.let { listMovies.add(it) }
        }
        return listMovies
    }

    @ExperimentalSerializationApi
    override suspend fun addNewAndGetUpdated(path:String,type:String) {
        val list = parseMovie(loadMoviesNet(path) as List<ResultsItem>)
        val newList = mutableListOf<MovieEntity>()
        for (i in list.indices) {
            convertToMovieEntity(list[i],type).let { newList.add(it) }
        }
        db.moviesDao.insertMovie(newList)
    }

    override fun getActors(movieId: Long): List<ActorsEntity> {
        return db.actorsDao.getAllActors(movieId)
    }

    override suspend fun insertActorsToDb(movieId: Long) {
        val newList = mutableListOf<ActorsEntity>()
        val actors: List<CastItem>? =
            RetrofitModule.moviesApi.getCast(movieId).cast as List<CastItem>?
        for (i in actors!!.indices) {
            val convertedActors = convertToActorsEntity(actors[i], movieId)
            newList.add(convertedActors)
        }
        db.actorsDao.insertActors(newList)
        // db.actorsDao.getAllActors(movieId)
    }
}