package com.example.androidacademyhomework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chootdev.recycleclick.RecycleClick
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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
            val bundle=arguments
            val pos:Int?=bundle?.getInt("pos")
                    actorRecycler = view.findViewById(R.id.actor_recycler_view)
                    actorRecycler?.apply {
                        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                        adapter = ActorListAdapter(loadMovies(requireContext()))
                        val imageBackDrop: ImageView = view.findViewById(R.id.orig)
                        Glide.with(view.context)
                            .load(pos?.let { loadMovies(requireContext()).get(it).backdrop })
                            .into(imageBackDrop)
                        val nameTitle: TextView = view.findViewById(R.id.name)
                        nameTitle.text = loadMovies(requireContext())[pos!!].title
                        val genreDetail: TextView = view.findViewById(R.id.tag)
                        val movie = loadMovies(requireContext())[pos].genres
                        val builder = StringBuilder()
                        for (n in movie) {
                            builder.append(n.name + ", ")
                        }
                        builder.deleteCharAt(builder.lastIndexOf(","));
                        genreDetail.text = builder.toString()
                        val overview: TextView = view.findViewById(R.id.after_the_d)
                        overview.text = loadMovies(requireContext())[pos].overview
                        val reviews: TextView = view.findViewById(R.id.reviews)
                        reviews.text = loadMovies(requireContext())[pos].numberOfRatings.toString() + " REVIEWS"
                }
            }
            super.onViewCreated(view, savedInstanceState)
            }
        }






