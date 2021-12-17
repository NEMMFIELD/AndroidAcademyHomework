package com.example.androidacademyhomework.ui

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
import com.example.androidacademyhomework.adapter.OnRecyclerItemClicked
import com.example.androidacademyhomework.adapter.UpcomingListAdapter
import com.example.androidacademyhomework.database.MovieEntity
import com.example.androidacademyhomework.databinding.FragmentUpcomingMoviesBinding
import com.example.androidacademyhomework.viewmodel.UpcomingViewModel
import com.example.androidacademyhomework.viewmodel.UpcomingViewModelFactory
import kotlinx.serialization.ExperimentalSerializationApi


class UpcomingMovies : Fragment() {
    private var _binding: FragmentUpcomingMoviesBinding? = null
    private val binding get() = _binding
    private val appContainer = MyApp.container

    @ExperimentalSerializationApi
    private val viewUpcomingModel: UpcomingViewModel by viewModels {
        UpcomingViewModelFactory(
            appContainer.moviesRepository
        )
    }
    private var upcomingListRecycler: RecyclerView? = null
    private lateinit var adapter: UpcomingListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpcomingMoviesBinding.inflate(inflater, container, false)
        val view = binding?.root
        return view!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        upcomingListRecycler = binding?.listRecyclerViewUpcoming
        if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            upcomingListRecycler?.layoutManager = GridLayoutManager(context, 2)
        }
        else
        {
            upcomingListRecycler?.layoutManager = GridLayoutManager(context, 4)
        }

        adapter = UpcomingListAdapter(
            clickListener = listener
        ) { movieEntity -> viewUpcomingModel.updateLike(movieEntity) }
        upcomingListRecycler?.adapter = adapter
        upcomingListRecycler?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                when {
                    !recyclerView.canScrollVertically(1) -> { //1 for down
                        viewUpcomingModel.loadMore(path = "upcoming", type = "upcoming")
                    }
                }
            }
        })
        viewUpcomingModel.upcomingAllMovies.observe(this.viewLifecycleOwner) { films ->
            films.let { adapter.submitList(it) }
        }
        viewUpcomingModel.insert(path = "upcoming", type = "upcoming")
    }

    private val listener = object : OnRecyclerItemClicked {
        override fun onClick(movie: MovieEntity) {
            val bundle = Bundle()
            movie.id?.let { bundle.putLong("arg", it) }
            view?.let {
                Navigation.findNavController(it)
                    .navigate(R.id.action_upcomingMovies_to_fragmentMoviesDetails, bundle)
            }
        }
    }

}