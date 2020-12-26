package com.example.androidacademyhomework

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
import com.bumptech.glide.Glide
import com.example.androidacademyhomework.data.Movie
import com.example.androidacademyhomework.data.loadMovies
import com.example.androidacademyhomework.viewholder.ActorListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FragmentMoviesDetails : Fragment() {
    private lateinit var movie: Movie
    private var actorRecycler: RecyclerView? = null
    private val scope = CoroutineScope(Job() + Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v: View = inflater.inflate(R.layout.fragment_movies_details, container, false)
        val backScr: TextView = v.findViewById(R.id.back)
        backScr.setOnClickListener {
            val fragment: Fragment = FragmentMoviesList()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        scope.launch {
            val bundle = arguments
            val pos: Int? = bundle?.getInt("pos")
            actorRecycler = view.findViewById(R.id.actor_recycler_view)
            actorRecycler?.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = ActorListAdapter(loadMovies(requireContext())[pos!!].actors)
            }
           bindDetails(view,pos)
        }
        super.onViewCreated(view, savedInstanceState)
    }
    private suspend fun bindDetails(view:View,position:Int?)
    {
        val imageBackDrop: ImageView = view.findViewById(R.id.orig)
        val nameTitle: TextView = view.findViewById(R.id.name)
        val genreDetail: TextView = view.findViewById(R.id.tag)
        val movie = loadMovies(requireContext())[position!!].genres
        val overview: TextView = view.findViewById(R.id.after_the_d)
        val reviews: TextView = view.findViewById(R.id.reviews)
        val ratingBar:RatingBar=view.findViewById(R.id.rating_bar)
        Glide.with(view.context)
            .load(position?.let { loadMovies(requireContext()).get(it).backdrop })
            .into(imageBackDrop)
        nameTitle.text = loadMovies(requireContext())[position!!].title
        val builder = StringBuilder()
        for (n in movie) {
            builder.append(n.name + ", ")
        }
        builder.deleteCharAt(builder.lastIndexOf(","));
        genreDetail.text = builder.toString()
        overview.text = loadMovies(requireContext())[position].overview
        reviews.text = loadMovies(requireContext())[position].numberOfRatings.toString() + " REVIEWS"
        ratingBar.rating= (loadMovies(requireContext())[position].ratings)*0.5F
    }
}






