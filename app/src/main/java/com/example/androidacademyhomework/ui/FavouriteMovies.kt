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
import com.example.androidacademyhomework.adapter.FavouriteListAdapter
import com.example.androidacademyhomework.adapter.OnRecyclerItemClicked
import com.example.androidacademyhomework.database.MovieEntity
import com.example.androidacademyhomework.databinding.FragmentFavouriteMoviesBinding
import com.example.androidacademyhomework.viewmodel.FavouriteMoviesViewModelFactory
import com.example.androidacademyhomework.viewmodel.FavouriteViewModel
import kotlinx.serialization.ExperimentalSerializationApi


class FavouriteMovies : Fragment() {
    private var _binding: FragmentFavouriteMoviesBinding? = null
    private val binding get() = _binding
    private val appContainer = MyApp.container

    @ExperimentalSerializationApi
    private val viewFavouriteModel: FavouriteViewModel by viewModels {
        FavouriteMoviesViewModelFactory(
            appContainer.moviesRepository
        )
    }
    private var favouriteListRecycler: RecyclerView? = null
    private lateinit var adapter: FavouriteListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouriteMoviesBinding.inflate(inflater, container, false)
        val view = binding?.root
        return view!!
    }

    @ExperimentalSerializationApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favouriteListRecycler = binding?.listRecyclerViewFavourite
        if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT) {
            favouriteListRecycler?.layoutManager = GridLayoutManager(context, 2)
        } else {
            favouriteListRecycler?.layoutManager = GridLayoutManager(context, 4)
        }
        adapter = FavouriteListAdapter(
            clickListener = listener
        )
        favouriteListRecycler?.adapter = adapter

        viewFavouriteModel.favouriteAllMovies.observe(this.viewLifecycleOwner) { films ->
            films.let { adapter.submitList(it) }
        }
    }

    private val listener = object : OnRecyclerItemClicked {
        override fun onClick(movie: MovieEntity) {
            val bundle = Bundle()
            movie.id?.let { bundle.putLong("arg", it) }
            view?.let {
                Navigation.findNavController(it)
                    .navigate(R.id.action_favouriteMovies_to_fragmentMoviesDetails, bundle)
            }
        }
    }
}