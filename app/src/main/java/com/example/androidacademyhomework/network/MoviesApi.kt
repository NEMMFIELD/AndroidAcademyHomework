package com.example.androidacademyhomework.network

import com.example.androidacademyhomework.data.model.viewholder.Actor
import com.example.androidacademyhomework.data.model.viewholder.Movie
import com.example.androidacademyhomework.data.model.viewholder.MovieInfo
import com.example.androidacademyhomework.data.model.viewholder.Response

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
interface MoviesApi {
    @GET("configuration?api_key=56b9fc3e2f7cf0c570b8d7dc71de180e")
    suspend fun getConfig(): Response

    @GET("movie/now_playing?api_key=56b9fc3e2f7cf0c570b8d7dc71de180e&language=en-US&page=")
    suspend fun getNowPlaying(@Query("page") page:Int): Movie

    @GET("movie/popular?api_key=56b9fc3e2f7cf0c570b8d7dc71de180e&language=en-US&page")
    suspend fun getPopular(@Query("page")page:Int):Movie

    @GET("movie/top_rated?api_key=56b9fc3e2f7cf0c570b8d7dc71de180e&language=en-US&page")
    suspend fun getTopRated(@Query("page")page:Int):Movie

    @GET("movie/upcoming?api_key=56b9fc3e2f7cf0c570b8d7dc71de180e&language=en-US&page")
    suspend fun getUpcoming(@Query("page")page:Int):Movie

    @GET("movie/{id}?api_key=56b9fc3e2f7cf0c570b8d7dc71de180e&language=en-US")
    suspend fun getMovieInfo(@Path("id")movieId:Int):MovieInfo

    @GET("movie/{id}/credits?api_key=56b9fc3e2f7cf0c570b8d7dc71de180e&language=en-US")
    suspend fun getCast(@Path("id")movieId:Int): Actor
}
