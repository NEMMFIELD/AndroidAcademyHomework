package com.example.androidacademyhomework

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androidacademyhomework.network.MovieRepo
import com.example.androidacademyhomework.ui.FragmentMoviesList

class MainActivity : AppCompatActivity() {
    lateinit var repository: MovieRepo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        repository = MovieRepo(applicationContext)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment, FragmentMoviesList.newInstance())
                .commit()
        }
    }
}