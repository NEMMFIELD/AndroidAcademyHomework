package com.example.androidacademyhomework.ui


import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidacademyhomework.MyApp
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.adapter.MovieListAdapter
import com.example.androidacademyhomework.adapter.OnRecyclerItemClicked
import com.example.androidacademyhomework.background.WorkRepository
import com.example.androidacademyhomework.database.MovieEntity
import com.example.androidacademyhomework.databinding.FragmentMoviesListBinding
import com.example.androidacademyhomework.viewmodel.MovieViewModel
import com.example.androidacademyhomework.viewmodel.MovieViewModelFactory
import com.example.androidacademyhomework.viewmodel.SharedViewModel
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.internal.notify


class FragmentMoviesList : Fragment() {
    private var _binding: FragmentMoviesListBinding? = null
    private val binding get() = _binding
    private val appContainer = MyApp.container

    private val workRepository = WorkRepository()

    @ExperimentalSerializationApi
    private val viewModel: MovieViewModel by viewModels { MovieViewModelFactory(appContainer.moviesRepository) }
    private var movieListRecycler: RecyclerView? = null
    private lateinit var adapter: MovieListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesListBinding.inflate(inflater, container, false)
        val view = binding?.root
        return view!!
    }

    @ExperimentalSerializationApi
    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // viewModel = ViewModelProvider(this, MovieViewModelFactory((requireActivity() as MainActivity).repository)).get(MovieViewModel::class.java)
        movieListRecycler = binding?.listRecyclerView
        val spanCount =
            if (activity?.resources?.configuration?.orientation != Configuration.ORIENTATION_PORTRAIT) {
                4
            } else {
                2
            }
        movieListRecycler?.layoutManager = GridLayoutManager(context, spanCount)
        adapter = MovieListAdapter(clickListener = listener) { movieEntity ->
            viewModel.updateLike(movieEntity)
        }
        movieListRecycler?.adapter = adapter
        // movieListRecycler?.itemAnimator=null
        movieListRecycler?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                when {
                    !recyclerView.canScrollVertically(1) -> { //1 for down
                        viewModel.loadMore("now_playing", "now_playing")
                    }
                }
            }
        })

        viewModel.allMovies.observe(this.viewLifecycleOwner) { films ->
            films.let { adapter.submitList(it) }
        }
        appContainer.workManager.enqueue(workRepository.periodicWork)
        /* WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
             "Send data",
             ExistingPeriodicWorkPolicy.KEEP,
             workRepository.periodicWork
         )*/
    }

    override fun onDestroy() {
        super.onDestroy()
        movieListRecycler = null
        appContainer.workManager.cancelAllWork()
    }

    override fun onDetach() {
        super.onDetach()
        appContainer.workManager.cancelAllWork()
    }

    private val listener = object : OnRecyclerItemClicked {
        override fun onClick(movie: MovieEntity) {
            val bundle = Bundle()
            movie.id?.let { bundle.putLong("arg", it) }
            view?.let {
                Navigation.findNavController(it)
                    .navigate(R.id.action_fragmentMoviesList_to_fragmentMoviesDetails, bundle)
            }
        }
    }
}