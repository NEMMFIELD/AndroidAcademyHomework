package com.example.androidacademyhomework.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.load
import coil.request.CachePolicy
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.androidacademyhomework.MyApp
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.Utils
import com.example.androidacademyhomework.adapter.ActorListAdapter
import com.example.androidacademyhomework.database.MovieEntity
import com.example.androidacademyhomework.databinding.FragmentMoviesDetailsBinding
import com.example.androidacademyhomework.viewmodel.MovieViewModel
import com.example.androidacademyhomework.viewmodel.MovieViewModelFactory
import kotlinx.serialization.ExperimentalSerializationApi

class FragmentMoviesDetails : Fragment() {
    private var _binding: FragmentMoviesDetailsBinding? = null
    private val binding get() = _binding
    private val appContainer = MyApp.container
    private var movieId: Long = 0

    @ExperimentalSerializationApi
    private val viewModel: MovieViewModel by viewModels { MovieViewModelFactory(appContainer.moviesRepository) }
    private var actorRecycler: RecyclerView? = null
    private lateinit var adapter: ActorListAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesDetailsBinding.inflate(inflater, container, false)
        val view = binding?.root
        binding?.back?.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        return view!!
    }

    @ExperimentalSerializationApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actorRecycler = binding?.actorRecyclerView
        movieId = arguments?.getLong("ID")!!
        println("MovieId=$movieId")
        val selectedMovie = appContainer.moviesRepository.getMovieById(movieId)
        println("Selected movie's title = ${selectedMovie.title}")
        println("Selected movie's backdrop = ${selectedMovie.detailImageUrl}")
        adapter = ActorListAdapter()
        fetchMovie(selectedMovie)
        actorRecycler?.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
        actorRecycler!!.adapter = adapter
        viewModel.actorList.observe(this.viewLifecycleOwner) { actors ->
            actors.let {
                adapter.submitList(it)
            }
        }
        binding?.calendarBtn?.setOnClickListener {
            Toast.makeText(requireContext(),"You click on calendar",Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    @ExperimentalSerializationApi
    private fun fetchMovie(movie: MovieEntity) = with(binding)
    {
       /* this?.orig?.load(Utils.backdropUrl + movie.detailImageUrl)
        {
            memoryCachePolicy(CachePolicy.ENABLED)
        }*/
        val circularProgressDrawable = CircularProgressDrawable(requireContext())
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        Glide.with(requireContext())
            .load(Utils.backdropUrl + movie.detailImageUrl)
            .placeholder(circularProgressDrawable)
            .apply(imageOption)
            .into(this!!.orig)
        this.afterTheD.text = movie.storyLine
        this.name.text = movie.title
        if (movie.pgAge) this.someId2.text = "16".plus("+")
        else this.someId2.text = "13".plus("+")
        this.tag.text = movie.genres?.joinToString { it }
        this.rating.stepSize = 0.5F
        this.rating.rating = movie.rating * 0.5F
        this.reviewsNumb.text = movie.reviewCount.toString().plus(" REVIEWS")
        movie.id?.let { viewModel.insertActor(it) }
    }

    companion object {
        private const val ID = "ID"
        fun newInstance(movieId: Long): FragmentMoviesDetails {
            val fragment = FragmentMoviesDetails()
            val args = Bundle()
            args.putLong(ID, movieId)
            fragment.arguments = args
            return fragment
        }

        private val imageOption = RequestOptions()
            .fallback(R.drawable.err)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
    }
}