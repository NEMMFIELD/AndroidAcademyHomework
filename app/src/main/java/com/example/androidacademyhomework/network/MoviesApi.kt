package com.example.androidacademyhomework.network

import com.example.androidacademyhomework.data.model.viewholder.Actor
import com.example.androidacademyhomework.data.model.viewholder.Movie
import com.example.androidacademyhomework.data.model.viewholder.MovieInfo
import com.example.androidacademyhomework.data.model.viewholder.Response


import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {
    @GET("configuration?")
    suspend fun getConfig(@Query("api_key") apiKey: String): Response

    @GET("movie/now_playing?&page")
    suspend fun getNowPlaying(
        @Query("api_key") apiKey: String,
        @Query("language")language:String,
        @Query("page") page: Int
    ): Movie

    @GET("movie/popular?&page")
    suspend fun getPopular(
        @Query("api_key") apiKey: String,
        @Query("language")language:String,
        @Query("page") page: Int
    ): Movie

    @GET("movie/top_rated?&page")
    suspend fun getTopRated(
        @Query("api_key") apiKey: String,
        @Query("language")language:String,
        @Query("page") page: Int
    ): Movie

    @GET("movie/upcoming?page")
    suspend fun getUpcoming(
        @Query("api_key") apiKey: String,
        @Query("language")language:String,
        @Query("page") page: Int
    ): Movie

    @GET("movie/{id}?&")
    suspend fun getMovieInfo(@Path("id") movieId: Int, @Query("api_key") apiKey: String): MovieInfo

    @GET("movie/{id}/credits?")
    suspend fun getCast(@Path("id") movieId: Int, @Query("api_key") apiKey: String, @Query("language")language:String,): Actor
}
