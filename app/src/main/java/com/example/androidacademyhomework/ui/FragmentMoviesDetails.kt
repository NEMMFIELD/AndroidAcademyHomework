package com.example.androidacademyhomework.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.androidacademyhomework.MyApp
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
    private var movieId :Long = 0

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
        val list = arguments?.getParcelable<MovieEntity>("key")
        //val filmId = arguments?.getLong("keyId")
        movieId = arguments?.getLong("ID")!!
        println("MovieId=$movieId")
        val selectedMovie = appContainer.moviesRepository.getMovieById(movieId)
        println("Selected movie's title = ${selectedMovie.title}")
        adapter = ActorListAdapter()
        fetchMovie(selectedMovie)
        actorRecycler?.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
        actorRecycler!!.adapter = adapter


       /* val networkConnection = NetworkConnection(requireContext())
        networkConnection.observe(viewLifecycleOwner, { isConnected ->
            if (isConnected) {
                if (list != null) {
                    fetchMovie(list)
                }
                viewModel.actorList.observe(this.viewLifecycleOwner) { actors ->
                    actors.let { adapter.submitList(it) }
                }
            } else {
                if (list != null) {
                    fetchMovie(list)
                }
                viewModel.actorList.observe(this.viewLifecycleOwner) { actors ->
                    actors.let { adapter.submitList(it) }
                }
                Toast.makeText(requireContext(), "Turn on internet", Toast.LENGTH_LONG).show()
            }
        })*/

         viewModel.actorList.observe(this.viewLifecycleOwner){actors->
             actors.let { adapter.submitList(it) }
         }
    }

    @SuppressLint("SetTextI18n")
    @ExperimentalSerializationApi
    private fun fetchMovie(movie: MovieEntity) {
        binding?.orig?.load(Utils.backdropUrl + movie.detailImageUrl)
        binding?.afterTheD?.text = movie.storyLine
        binding?.name?.text = movie.title
        if (movie.pgAge) binding?.someId2?.text = "16".plus("+")
        else binding?.someId2?.text = "13".plus("+")
        binding?.tag?.text = movie.genres?.joinToString { it }
        binding?.rating?.stepSize = 0.5F
        binding?.rating?.rating = movie.rating * 0.5F
        binding?.reviewsNumb?.text = movie.reviewCount.toString().plus(" REVIEWS")

        movie.id?.let{viewModel.insertActor(it)}
        /*val networkConnection = NetworkConnection(requireContext())
        networkConnection.observe(viewLifecycleOwner, { isConnected ->
            if (isConnected) {
                movie.id?.let { viewModel.insertActor(it) }
            } else {
                movie.id?.let { viewModel.getActors(it) }
            }
        })*/
    }
    companion object {
        private const val  ID = "ID"
        fun newInstance(movieId: Long): FragmentMoviesDetails {
            val fragment = FragmentMoviesDetails()
            val args = Bundle()
            args.putLong(ID, movieId)
            fragment.arguments = args
            return fragment
        }
    }
}