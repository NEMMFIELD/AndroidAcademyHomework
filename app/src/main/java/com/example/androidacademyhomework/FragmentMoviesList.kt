package com.example.androidacademyhomework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation

class FragmentMoviesList : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.fragment_movies_list, container, false)
        val nextScr: ImageView = v.findViewById(R.id.movie_img)
        nextScr.setOnClickListener { view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.navigateToSecond) } }
        return v
    }

}
