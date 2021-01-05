package com.example.androidacademyhomework.View.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.data.model.Movie
import com.example.androidacademyhomework.data.model.viewholder.CellClickListener
import com.example.androidacademyhomework.data.model.viewholder.MovieListAdapter
import com.example.androidacademyhomework.viewmodel.MovieListViewModel
import com.example.androidacademyhomework.viewmodel.MovieListViewModelFactory

class FragmentMoviesList : Fragment(), CellClickListener {
    private var movieListRecycler: RecyclerView? = null
    private val viewModel: MovieListViewModel by viewModels {
        MovieListViewModelFactory(
            requireContext()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.fragment_movies_list, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setUpMoviesListAdapter()
        viewModel.movieList.observe(this.viewLifecycleOwner, this::updateAdapter)
    }

    override fun onDestroy() {
        super.onDestroy()
        movieListRecycler?.adapter = null
        movieListRecycler = null
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadData()
    }

    private fun updateAdapter(movies: List<Movie>) {
        (movieListRecycler?.adapter as? MovieListAdapter)?.apply {
            bindMovies(movies)
        }
    }

    private fun initViews() {
        movieListRecycler = view?.findViewById(R.id.list_recycler_view)
    }

    private fun setUpMoviesListAdapter() {
        movieListRecycler?.layoutManager = GridLayoutManager(activity, 2)
        movieListRecycler?.adapter =
            MovieListAdapter(viewModel.movieList.value!!, this@FragmentMoviesList)
    }

    override fun onCellClickListener(view: View, position: Int) {
        val fragment: Fragment = FragmentMoviesDetails()
        val bundle = Bundle()
        bundle.putInt("pos", position)
        fragment.arguments = bundle
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}





