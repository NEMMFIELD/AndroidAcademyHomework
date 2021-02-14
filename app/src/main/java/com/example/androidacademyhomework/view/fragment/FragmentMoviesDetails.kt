package com.example.androidacademyhomework.view.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.data.model.viewholder.ActorListAdapter
import com.example.androidacademyhomework.data.model.viewholder.CastItem
import com.example.androidacademyhomework.data.model.viewholder.Movie
import com.example.androidacademyhomework.data.model.viewholder.ResultsItem
import com.example.androidacademyhomework.database.DbViewModel
import com.example.androidacademyhomework.database.actor.ActorDb
import com.example.androidacademyhomework.network.*
import com.example.androidacademyhomework.viewmodel.MovieListViewModel
import com.example.androidacademyhomework.viewmodel.MovieListViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class FragmentMoviesDetails : Fragment() {
    private lateinit var dbViewModel: DbViewModel
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
        scope.launch {
            setUpMoviesDetailsAdapter(pos)
            if (pos != null) {
                bindDetails(pos)
            }
        }
        if (pos != null) {
            viewModel.loadActors(pos)
        }
        viewModel.actorList.observe(this.viewLifecycleOwner, this::updateDetailsAdapter)
        dbViewModel = ViewModelProvider(this).get(DbViewModel::class.java)
        scope.launch {
            if (pos != null) {
                insertActorsDb(pos)
            }
        }
    }

    private fun updateDetailsAdapter(actors: List<CastItem?>?) {
        (actorRecycler?.adapter as? ActorListAdapter)?.apply {
            bindActors(actors)
        }
    }

    private fun initViews() {
        actorRecycler = view?.findViewById(R.id.actor_recycler_view)
        imageBackDrop = view?.findViewById(R.id.orig)
        nameTitle = view?.findViewById(R.id.name)
        genreDetail = view?.findViewById(R.id.tag)
        overview = view?.findViewById(R.id.after_the_d)
        reviews = view?.findViewById(R.id.reviews)
        ratingBar = view?.findViewById(R.id.rating_bar)
    }

    private suspend fun setUpMoviesDetailsAdapter(pos: Int?) {
        actorRecycler?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val movie: Movie = RetrofitModule.moviesApi.getNowPlaying(API_KEY, LANGUAGE, PAGE_NUMB)
        val movieId: Long? = movie.results?.get(pos!!)?.id
        actorRecycler?.adapter =
            ActorListAdapter(RetrofitModule.moviesApi.getCast(movieId!!, API_KEY, LANGUAGE).cast)
    }

    override fun onDestroy() {
        super.onDestroy()
        actorRecycler?.adapter = null
        actorRecycler = null
    }

    private suspend fun insertActorsDb(pos:Int) {
        val movie: Movie = RetrofitModule.moviesApi.getNowPlaying(API_KEY, LANGUAGE, PAGE_NUMB)
        val movieId: Long? = pos.let { movie.results?.get(it)?.id }
        for (i in RetrofitModule.moviesApi.getCast(movieId!!, API_KEY, LANGUAGE).cast!!.indices) {
            val actorDb = ActorDb(
                movieId.let {
                    RetrofitModule.moviesApi.getCast(
                        it,
                        API_KEY,
                        LANGUAGE
                    ).cast?.get(i)?.id
                }!!,
                movieId,
                RetrofitModule.moviesApi.getCast(movieId, API_KEY, LANGUAGE).cast?.get(i)?.name,
                RetrofitModule.moviesApi.getCast(
                    movieId,
                    API_KEY,
                    LANGUAGE
                ).cast?.get(i)?.profilePath
            )
            dbViewModel.addActor(actorDb)
        }

        Toast.makeText(requireContext(), "ActorDb successfully added!", Toast.LENGTH_SHORT)
            .show()
    }

        private suspend fun bindDetails(position: Int) {
            val movie = RetrofitModule.moviesApi.getNowPlaying(API_KEY, LANGUAGE, PAGE_NUMB)
            val config = RetrofitModule.moviesApi.getConfig(API_KEY)
            val movieInfoRequest =
                RetrofitModule.moviesApi.getMovieInfo(movie.results?.get(position!!)?.id!!, API_KEY)
            val backImgUrl: String =
                config.images?.secureBaseUrl + config.images?.backdropSizes?.get(2) + movie.results[position]?.backdropPath
            imageBackDrop?.load(backImgUrl)
            nameTitle?.text = position.let { movie.results[it]?.title }
            genreDetail?.text = movieInfoRequest.genres?.map { it!!.name }!!.joinToString()
            overview?.text = movieInfoRequest.overview
            reviews?.text = movieInfoRequest.voteCount.toString().plus(" REVIEWS")
            ratingBar?.rating = movie.results[position]?.voteAverage!! * 0.5F
        }
    }



