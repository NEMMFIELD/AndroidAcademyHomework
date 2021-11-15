package com.example.androidacademyhomework.network

import com.example.androidacademyhomework.network.pojopack.ActorsResponse
import com.example.androidacademyhomework.network.pojopack.Configuration
import com.example.androidacademyhomework.network.pojopack.Movie
import com.example.androidacademyhomework.network.pojopack.MovieInfo
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MoviesApi {
        @GET("configuration?")
        suspend fun getConfig(@Query("api_key") apiKey: String): Configuration

        @GET("https://api.themoviedb.org/3/movie/now_playing?api_key=56b9fc3e2f7cf0c570b8d7dc71de180e&language=en-US&page")
        suspend fun getNowPlaying(@Query("page")page:Int): Movie

        @GET("https://api.themoviedb.org/3/movie/{movie_id}?api_key=56b9fc3e2f7cf0c570b8d7dc71de180e&language=en-US")
        suspend fun getMoviesInfo(@Path("movie_id") movieId: Long):MovieInfo
        
        @GET("https://api.themoviedb.org/3/movie/{movie_id}/credits?api_key=56b9fc3e2f7cf0c570b8d7dc71de180e&language=en-US")
        suspend fun getCast(@Path("movie_id")movieId:Long):ActorsResponse
}