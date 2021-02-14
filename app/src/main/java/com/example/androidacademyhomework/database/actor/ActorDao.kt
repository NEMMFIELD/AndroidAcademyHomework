package com.example.androidacademyhomework.database.actor

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.androidacademyhomework.database.movie.MovieDb

@Dao
interface ActorDao {
    @Query("SELECT * FROM actorslist")
    fun getAllActors(): LiveData<List<ActorDb>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addActor(actor: ActorDb)

    @Insert
    fun insertAllActors(actors: MutableList<ActorDb>)

    @Delete
    fun deleteActor(deletedActor: ActorDb)
}