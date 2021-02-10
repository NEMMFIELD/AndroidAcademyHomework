package com.example.androidacademyhomework.view

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.data.model.viewholder.ResultsItem
import com.example.androidacademyhomework.database.AppDatabase
import com.example.androidacademyhomework.database.MovieDao
import com.example.androidacademyhomework.network.API_KEY
import com.example.androidacademyhomework.network.LANGUAGE
import com.example.androidacademyhomework.network.RetrofitModule
import com.example.androidacademyhomework.view.fragment.FragmentMoviesList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment, FragmentMoviesList())
                .commit()
        }
    }
}