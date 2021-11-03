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
import androidx.work.WorkManager
import com.example.androidacademyhomework.background.WorkRepository
import com.example.androidacademyhomework.databinding.ActivityMainBinding
import com.example.androidacademyhomework.network.NetworkConnection
import kotlinx.serialization.ExperimentalSerializationApi

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var repository: MovieRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        repository = MovieRepo(applicationContext)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment, FragmentMoviesList.newInstance())
                .commit()
        }

    }
}