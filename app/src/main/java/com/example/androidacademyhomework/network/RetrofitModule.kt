package com.example.androidacademyhomework.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

 object RetrofitModule {

     @Suppress("EXPERIMENTAL_API_USAGE")
     private val retrofit: Retrofit = Retrofit.Builder()
         .baseUrl("https://api.themoviedb.org/3/")
         .addConverterFactory(GsonConverterFactory.create())
         .build()

     val moviesApi: MoviesApi = retrofit.create()
 }