package com.example.androidacademyhomework

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView:TextView=findViewById(R.id.textView_1)
        textView.setOnClickListener { openActivityMovieDetails() }

    }

    private fun openActivityMovieDetails()
    {
        val intent= Intent(this,MovieDetailsActivity::class.java)
        startActivity(intent)
    }
}