package com.example.androidacademyhomework.view.fragment


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
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.data.model.viewholder.ActorListAdapter
import com.example.androidacademyhomework.data.model.viewholder.Movie
import com.example.androidacademyhomework.data.model.viewholder.MovieListAdapter
import com.example.androidacademyhomework.network.RetrofitModule
import com.example.androidacademyhomework.viewmodel.MovieListViewModel
import com.example.androidacademyhomework.viewmodel.MovieListViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentMoviesDetails : Fragment() {
    private var actorRecycler: RecyclerView? = null
    private var imageBackDrop: ImageView? = null
    private var nameTitle: TextView? = null
    private var genreDetail: TextView? = null
    private var overview: TextView? = null
    private var reviews: TextView? = null
    private var ratingBar: RatingBar? = null
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    private val viewModel: MovieListViewModel by viewModels {
        MovieListViewModelFactory(
            requireContext()
        )
    }

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
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        val pos: Int? = bundle?.getInt("pos")
        initViews()
        scope.launch { setUpMoviesDetailsAdapter(pos) }
        // bindDetails(pos)
        // viewModel.loadActors(pos!!)
        // viewModel.actorList.observe(this.viewLifecycleOwner, this::updateDetailsAdapter)
    }

    /* private fun updateDetailsAdapter(actors: List<Actor>) {
         (actorRecycler?.adapter as? ActorListAdapter)?.apply {
             bindActors(actors)
         }
     }*/

    private fun initViews() {
        actorRecycler = view?.findViewById(R.id.actor_recycler_view)
        imageBackDrop = view?.findViewById(R.id.orig)
        nameTitle = view?.findViewById(R.id.name)
        genreDetail = view?.findViewById(R.id.tag)
        overview = view?.findViewById(R.id.after_the_d)
        reviews = view?.findViewById(R.id.reviews)
        ratingBar = view?.findViewById(R.id.rating_bar)
    }

    private suspend fun setUpMoviesDetailsAdapter(pos:Int?) {

        actorRecycler?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val movie:Movie=RetrofitModule.moviesApi.getNowPlaying(1)
        val movieId:Int?= movie.results?.get(pos!!)?.id
        actorRecycler?.adapter = ActorListAdapter(RetrofitModule.moviesApi.getCast(movieId!!))
    }

    override fun onDestroy() {
        super.onDestroy()
        actorRecycler?.adapter = null
        actorRecycler = null
    }


    /* private fun bindDetails(position: Int?) {
         scope.launch {
             val movie = loadMovies(requireContext())[position!!].genres
             imageBackDrop?.load(loadMovies(requireContext())[position].backdrop)
             nameTitle?.text = loadMovies(requireContext())[position].title
             val builder = StringBuilder()
             for (n in movie) {
                 builder.append(n.name + ", ")
             }
             builder.deleteCharAt(builder.lastIndexOf(","));
             genreDetail?.text = builder.toString()
             overview?.text = loadMovies(requireContext())[position].overview
             reviews?.text =
                 loadMovies(requireContext())[position].numberOfRatings.toString() + " REVIEWS"
             ratingBar?.rating = loadMovies(requireContext())[position].ratings.times(0.5F)
         }
     }*/
}




