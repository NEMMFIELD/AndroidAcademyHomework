package com.example.androidacademyhomework

import android.app.Application

class Utils:Application() {
    companion object
    {
        //global var:
        var page = 1
        const val posterUrl = "https://image.tmdb.org/t/p/w780"
        const val backdropUrl = "https://image.tmdb.org/t/p/w1280"
        const val actorUrl = "https://image.tmdb.org/t/p/h632"
        const val apiKey = "56b9fc3e2f7cf0c570b8d7dc71de180e"
    }

}