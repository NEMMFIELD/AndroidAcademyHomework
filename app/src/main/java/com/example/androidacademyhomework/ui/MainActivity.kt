package com.example.androidacademyhomework.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.androidacademyhomework.MyApp
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
       /* if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment, FragmentMoviesList.newInstance())
                .commit()
        }*/
        intent?.let(::handleIntent)
        binding.bottomNavigation?.setOnItemSelectedListener {item ->
            when (item.itemId)
            {
                R.id.page_now -> {
                    Toast.makeText(this,"You are on now play page", Toast.LENGTH_SHORT).show()
                    /*    supportFragmentManager.beginTransaction()
                            .add(R.id.fragment, FragmentMoviesList.newInstance())
                            .commit()*/
                    return@setOnItemSelectedListener true
                }
                R.id.page_popular ->
                {
                    Toast.makeText(this,"You are on popular page", Toast.LENGTH_SHORT).show()
                      /*  supportFragmentManager.beginTransaction()
                            .add(R.id.fragment, PopularMovies.newInstance())
                            .commit()*/
                    return@setOnItemSelectedListener true
                }
                R.id.page_favor ->
                {
                    Toast.makeText(this,"You are on favourite page", Toast.LENGTH_SHORT).show()
                    return@setOnItemSelectedListener true
                }
                R.id.page_top ->
                {
                    Toast.makeText(this,"You are on top page", Toast.LENGTH_SHORT).show()
                    return@setOnItemSelectedListener true
                }
                else->
                {
                    Toast.makeText(this,"You are on upcoming page", Toast.LENGTH_SHORT).show()
                    return@setOnItemSelectedListener false
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            handleIntent(intent)
        }
    }

    private fun handleIntent(intent: Intent) {
        when (intent.action) {
            Intent.ACTION_VIEW -> {
                val movieId = intent.data?.lastPathSegment?.toLongOrNull()
                if (movieId != null) {
                   supportFragmentManager.beginTransaction()
                        .add(R.id.fragment, FragmentMoviesDetails.newInstance(movieId))
                        .addToBackStack(null)
                        .commit()
                }
                if (movieId != null) {
                    MyApp.container.moviesNotification.dismissNotification(movieId = movieId)
                }
            }
        }
    }
}