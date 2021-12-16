package com.example.androidacademyhomework.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.androidacademyhomework.MyApp
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController:NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.activity_main_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.activity_main_bottom_navigation_view).setupWithNavController(
            navController
        )
        intent?.let(::handleIntent)
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
                   /* supportFragmentManager.beginTransaction()
                        .add(R.id.fragment, FragmentMoviesDetails.newInstance(movieId))
                        .addToBackStack(null)
                        .commit()*/
                    val bundle = Bundle()
                    movieId.let { bundle.putLong("arg", it) }
                       //Navigation.findNavController(supportFragmentManager.findFragmentById(R.id.activity_main_nav_host_fragment))
                    // .navigate(R.id.action_fragmentMoviesList_to_fragmentMoviesDetails, bundle)
                   /* val pendingIntent = NavDeepLinkBuilder(this)
                        .setGraph(R.navigation.my_nav)
                        .setDestination(R.id.fragmentMoviesDetails)
                        .setArguments(bundle)
                        .createPendingIntent()*/
                    navController.navigate(R.id.action_global_fragmentMoviesDetails,bundle)

                }
                if (movieId != null) {
                    MyApp.container.moviesNotification.dismissNotification(movieId = movieId)
                }
            }
        }
    }
}