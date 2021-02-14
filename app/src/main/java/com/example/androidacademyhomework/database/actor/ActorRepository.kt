package com.example.androidacademyhomework.database.actor

import androidx.lifecycle.LiveData
import com.example.androidacademyhomework.database.movie.MovieDao
import com.example.androidacademyhomework.database.movie.MovieDb

class ActorRepository(private val actorDao: ActorDao?) {
    val readAllActors: LiveData<List<ActorDb>> = actorDao!!.getAllActors()

    fun addActor(actor: ActorDb){
        actorDao?.addActor(actor)
    }

}