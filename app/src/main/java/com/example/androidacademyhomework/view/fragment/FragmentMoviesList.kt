package com.example.androidacademyhomework.view.fragment

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
import com.example.androidacademyhomework.data.model.viewholder.CellClickListener
import com.example.androidacademyhomework.data.model.viewholder.Movie
import com.example.androidacademyhomework.data.model.viewholder.MovieListAdapter
import com.example.androidacademyhomework.data.model.viewholder.ResultsItem
import com.example.androidacademyhomework.network.API_KEY
import com.example.androidacademyhomework.network.LANGUAGE
import com.example.androidacademyhomework.network.PAGE_NUMB
import com.example.androidacademyhomework.network.RetrofitModule
import com.example.androidacademyhomework.viewmodel.MovieListViewModel
import com.example.androidacademyhomework.viewmodel.MovieListViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class FragmentMoviesList : Fragment(), CellClickListener {
    private var movieListRecycler: RecyclerView? = null
    private var isLoading: Boolean = false
    private lateinit var layoutManager: GridLayoutManager
    private var scope = CoroutineScope(Dispatchers.Main)
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
        return inflater.inflate(R.layout.fragment_movies_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        viewModel.loadData()
        scope.launch { setUpMoviesListAdapter() }
        movieListRecycler?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItem: Int = layoutManager.itemCount
                val lastVisibleItem: Int = layoutManager.findLastVisibleItemPosition()
                scope.launch {
                    if (!isLoading && lastVisibleItem == totalItem - 1) {
                        isLoading = true
                        PAGE_NUMB++
                        val existingAdapter = movieListRecycler?.adapter as? MovieListAdapter
                        existingAdapter?.addItems(
                          loadMovies()
                        )
                        isLoading = false
                    }

                }
            }
        })
        viewModel.movieList.observe(this.viewLifecycleOwner, this::updateAdapter)
    }

    override fun onDestroy() {
        super.onDestroy()
        movieListRecycler?.adapter = null
        movieListRecycler = null
    }

      private fun updateAdapter(movies: List<ResultsItem>) {
        (movieListRecycler?.adapter as? MovieListAdapter)?.apply {
          addItems(movies)
    }
    }

    private fun initViews() {
        movieListRecycler = view?.findViewById(R.id.list_recycler_view)
    }

    private suspend fun setUpMoviesListAdapter() {
        layoutManager = GridLayoutManager(activity, 2)
        movieListRecycler?.layoutManager = layoutManager
        movieListRecycler?.adapter = MovieListAdapter(
            loadMovies(), this@FragmentMoviesList
        )
    }

    private suspend fun loadMovies():MutableList<ResultsItem>
    {
        val result = RetrofitModule.moviesApi.getNowPlaying(API_KEY, LANGUAGE, PAGE_NUMB).results as MutableList<ResultsItem>
        return result
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




