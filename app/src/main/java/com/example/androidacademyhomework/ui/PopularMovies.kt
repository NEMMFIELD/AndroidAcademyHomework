package com.example.androidacademyhomework.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidacademyhomework.MyApp
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.adapter.MovieListAdapter
import com.example.androidacademyhomework.adapter.OnRecyclerItemClicked
import com.example.androidacademyhomework.adapter.PopularListAdapter
import com.example.androidacademyhomework.database.MovieEntity
import com.example.androidacademyhomework.databinding.FragmentPopularMoviesBinding
import com.example.androidacademyhomework.viewmodel.MovieViewModel
import com.example.androidacademyhomework.viewmodel.MovieViewModelFactory
import kotlinx.serialization.ExperimentalSerializationApi


class PopularMovies : Fragment() {
    private var _binding: FragmentPopularMoviesBinding? = null
    private val binding get() = _binding
    private val appContainer = MyApp.container
    private val viewModel: MovieViewModel by viewModels { MovieViewModelFactory(appContainer.moviesRepository) }
    private var movieListRecycler: RecyclerView? = null
    private lateinit var adapter: PopularListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPopularMoviesBinding.inflate(inflater, container, false)
        val view = binding?.root
        return view!!
    }

    @ExperimentalSerializationApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieListRecycler = binding?.listRecyclerViewPopular
        movieListRecycler?.layoutManager = GridLayoutManager(context, 2)
        adapter = PopularListAdapter(
            clickListener = listener
        ) { movieEntity -> viewModel.updateLike(movieEntity) }
        movieListRecycler?.adapter = adapter
        movieListRecycler?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                when {
                    !recyclerView.canScrollVertically(1) -> { //1 for down
                        viewModel.loadMore(path = "popular",type ="popular")
                    }
                }
            }
        })
      //  viewModel.allMovies.observe(this.viewLifecycleOwner) { films ->
       //     films.let { adapter.submitList(it) }
       // }
      //  viewModel.insert(path = "popular",type = "popular")
    }

    private val listener = object : OnRecyclerItemClicked {
        override fun onClick(movie: MovieEntity) {
            parentFragmentManager.beginTransaction()
                .add(R.id.fragment, FragmentMoviesDetails.newInstance(movie.id!!))
                .addToBackStack(null)
                .commit()
        }
    }

    companion object {
        fun newInstance() = PopularMovies()
    }
}