package com.example.androidacademyhomework

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.data.model.viewholder.ClickListener
import com.example.androidacademyhomework.data.model.viewholder.ResultsItem
import com.example.androidacademyhomework.view.fragment.FragmentMoviesDetails
import com.example.androidacademyhomework.view.fragment.FragmentMoviesList

class MainActivity : AppCompatActivity(),ClickListener{
    private val movieListFragment = FragmentMoviesList.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment, movieListFragment)
                .commit()
        }
    }
     override fun changeFragment(showedFragment: Fragment, movie: ResultsItem?) {
        val nextFragment = when(showedFragment) {
            is FragmentMoviesDetails -> movieListFragment
            is FragmentMoviesList -> FragmentMoviesDetails.newInstance(movie)
            else -> null
        }
        supportFragmentManager.beginTransaction().apply {
            if (nextFragment != null) {
                replace(R.id.fragment, nextFragment)
            }
            addToBackStack(null)
            commit()
        }
    }
}