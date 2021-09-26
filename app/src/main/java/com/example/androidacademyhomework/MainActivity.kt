package com.example.androidacademyhomework

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androidacademyhomework.database.MovieDataBase
import com.example.androidacademyhomework.network.MovieRepo
import com.example.androidacademyhomework.ui.FragmentMoviesList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

class MainActivity : AppCompatActivity() {
    lateinit var repository: MovieRepo
    private var scope= CoroutineScope(Dispatchers.Main)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        repository = MovieRepo(applicationContext)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment, FragmentMoviesList.newInstance())
                .commit()
        }

        //scope.launch { db.moviesDao.getAllMovies() }
    }
}