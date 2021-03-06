package com.example.androidacademyhomework.view.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.data.model.viewholder.ActorListAdapter
import com.example.androidacademyhomework.data.model.viewholder.CastItem
import com.example.androidacademyhomework.data.model.viewholder.ClickListener
import com.example.androidacademyhomework.data.model.viewholder.ResultsItem
import com.example.androidacademyhomework.database.DbViewModel
import com.example.androidacademyhomework.network.API_KEY
import com.example.androidacademyhomework.network.RetrofitModule
import com.example.androidacademyhomework.viewmodel.ActorListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class FragmentMoviesDetails(private var movie: ResultsItem? = null) : Fragment() {
    private lateinit var dbViewModel: DbViewModel
    private var actorRecycler: RecyclerView? = null
    private var imageBackDrop: ImageView? = null
    private lateinit var adapter: ActorListAdapter
    private var backTextView: TextView? = null
    private var nameTitle: TextView? = null
    private var genreDetail: TextView? = null
    private var overview: TextView? = null
    private var listener: ClickListener? = null
    private var reviews: TextView? = null
    private var ratingBar: RatingBar? = null
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    companion object {
        const val KEY_MOVIE_EX = "KEY_MOVIE_EX"
        fun newInstance(movie: ResultsItem?) = FragmentMoviesDetails(movie)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            movie = savedInstanceState.getSerializable(KEY_MOVIE_EX) as ResultsItem?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_movies_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backTextView = view.findViewById(R.id.back)
        backTextView?.setOnClickListener {
            listener?.changeFragment(this, null)
        }
        initViews()
        setUpMoviesDetailsAdapter()
        scope.launch { bindDetails(movie!!) }
        //dbViewModel = ViewModelProvider(this).get(DbViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        listener = activity as ClickListener
        backTextView?.setOnClickListener {
            listener?.changeFragment(this, null)
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

    private fun setUpMoviesDetailsAdapter() {
        actorRecycler?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = ActorListAdapter()
        actorRecycler?.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        actorRecycler?.adapter = null
        actorRecycler = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(KEY_MOVIE_EX, movie)
    }

    private suspend fun bindDetails(movie: ResultsItem) {
        val config = RetrofitModule.moviesApi.getConfig(API_KEY)
        val movieInfoRequest = RetrofitModule.moviesApi.getMovieInfo(movie.id!!, API_KEY)
        val backImgUrl: String =
            config.images?.secureBaseUrl + config.images?.backdropSizes?.get(2) + movie.backdropPath
        imageBackDrop?.load(backImgUrl)
        nameTitle?.text = movie.title
        genreDetail?.text = movieInfoRequest.genres?.map { it!!.name }!!.joinToString()
        overview?.text = movieInfoRequest.overview
        reviews?.text = movieInfoRequest.voteCount.toString().plus(" REVIEWS")
        ratingBar?.rating = movie.voteAverage!! * 0.5F

        val viewModel = ActorListViewModel()
        viewModel.loadActorList(movieId = movie.id)
        viewModel.actorList.observe(this.viewLifecycleOwner, this::updateDetailsAdapter)
    }
}
/* private suspend fun insertActorsDb(pos: Int) {
   val movie = RetrofitModule.moviesApi.getNowPlaying(API_KEY, LANGUAGE, PAGE_NUMB)
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
}*/




