package com.example.androidacademyhomework.network

import retrofit2.http.GET
import retrofit2.http.Query

const val apiKey = "56b9fc3e2f7cf0c570b8d7dc71de180e"

interface MoviesApi {
        @GET("configuration?")
        suspend fun getConfig(@Query("api_key") apiKey: String): Configuration

        @GET("https://api.themoviedb.org/3/movie/now_playing?api_key=56b9fc3e2f7cf0c570b8d7dc71de180e&language=en-US&page=1")
        suspend fun getNowPlaying(): Movie
}