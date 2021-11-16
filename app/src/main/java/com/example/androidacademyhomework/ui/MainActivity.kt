package com.example.androidacademyhomework.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment, FragmentMoviesList.newInstance())
                .commit()
        }
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
                  /*  val fragment: Fragment = FragmentMoviesDetails()
                    val fragmentManager: FragmentManager = this.supportFragmentManager
                    val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                    val args = Bundle()
                    args.putLong("keyId", movieId)
                    fragment.arguments = args
                    fragmentTransaction.add(R.id.fragment, fragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()*/
                   supportFragmentManager.beginTransaction()
                        .add(R.id.fragment, FragmentMoviesDetails.newInstance(movieId))
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
    }
}