package com.example.androidacademyhomework.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.Utils
import com.example.androidacademyhomework.adapter.ActorListAdapter
import com.example.androidacademyhomework.database.MovieEntity
import com.example.androidacademyhomework.model.Model
import com.example.androidacademyhomework.network.pojopack.CastItem

class FragmentMoviesDetails : Fragment() {
    private var actorRecycler: RecyclerView? = null
    private var listOfActors: List<CastItem>? = listOf()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v: View = inflater.inflate(R.layout.fragment_movies_details, container, false)
        val backScr: TextView = v.findViewById(R.id.back)
        backScr.setOnClickListener {
           // val fragment: Fragment = FragmentMoviesList()
           // val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
           // val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
           // fragmentTransaction.replace(R.id.fragment, fragment)
            //fragmentTransaction.addToBackStack(null)
            //fragmentTransaction.commit()
            parentFragmentManager.popBackStack()
        }
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actorRecycler = view.findViewById(R.id.actor_recycler_view)
        val list = arguments?.getParcelable<MovieEntity>("key")
        //listOfActors = list?.actors
        if (list != null) {
            fetchMovie(list)
        }
        actorRecycler?.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = listOfActors?.let { ActorListAdapter(it) }
        }
    }

    private fun fetchMovie(movie: MovieEntity) {
        val backgroundImage = view?.findViewById<ImageView>(R.id.orig)
        val description = view?.findViewById<TextView>(R.id.after_the_d)
        val name = view?.findViewById<TextView>(R.id.name)
        val ageRate = view?.findViewById<TextView>(R.id.some_id2)
        val jenres = view?.findViewById<TextView>(R.id.tag)
        val rating = view?.findViewById<RatingBar>(R.id.rating)
        val numReviews = view?.findViewById<TextView>(R.id.reviewsNumb)
        backgroundImage?.load(Utils.backdropUrl + movie.detailImageUrl)
        description?.text = movie.storyLine
        name?.text = movie.title
        if (movie.pgAge) ageRate?.text = "16".plus("+")
        else ageRate?.text = "13".plus("+")
        jenres?.text = movie.genres?.joinToString { it }
        rating?.stepSize = 0.5F
        rating?.rating = movie.rating * 0.5F
        numReviews?.text = movie.reviewCount.toString().plus(" REVIEWS")
    }
}