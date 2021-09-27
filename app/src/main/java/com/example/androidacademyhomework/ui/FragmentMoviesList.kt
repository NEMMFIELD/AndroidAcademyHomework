package com.example.androidacademyhomework.ui


import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidacademyhomework.MainActivity
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.adapter.MovieListAdapter
import com.example.androidacademyhomework.adapter.OnRecyclerItemClicked
import com.example.androidacademyhomework.database.MovieEntity
import com.example.androidacademyhomework.model.Model
import com.example.androidacademyhomework.viewmodel.MovieViewModel
import com.example.androidacademyhomework.viewmodel.MovieViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi

class FragmentMoviesList : Fragment() {
    private val viewModel: MovieViewModel by viewModels { MovieViewModelFactory((requireActivity() as MainActivity).repository) }
    private var movieListRecycler: RecyclerView? = null
    private lateinit var adapter: MovieListAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_movies_list, container, false)
    }

    override fun onDetach() {
       // movieListRecycler = null
        // scope.cancel()
        super.onDetach()
    }

    @ExperimentalSerializationApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // viewModel = ViewModelProvider(
        //    this,
        //     MovieViewModelFactory((requireActivity() as MainActivity).repository)
        // ).get(MovieViewModel::class.java)
        movieListRecycler = view.findViewById(R.id.list_recycler_view)
        adapter = MovieListAdapter(clickListener = listener)
        movieListRecycler?.layoutManager = GridLayoutManager(context, 2)
        movieListRecycler?.adapter = adapter
        movieListRecycler?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                when {
                    !recyclerView.canScrollVertically(1) -> { //1 for down
                         viewModel.loadMore()
                    }
                }
            }
        })

       lifecycleScope.launch{
            viewModel.moviesList.collect {result ->
                updateData(result)
            }
        }
       // viewModel.moviesList.observe(this.viewLifecycleOwner, this::updateData)
      // Log.d("Page", page.toString())
    }

    private fun updateData(movies: List<MovieEntity>) {
        adapter.bindMovies(movies)
    }

    private fun doOnClick(movie: MovieEntity) {
        movieListRecycler?.let { rv ->
            Snackbar.make(
                rv,
                "You chose: {${movie.title}}",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private val listener = object : OnRecyclerItemClicked {
        override fun onClick(movie: MovieEntity) {
            val fragment: Fragment = FragmentMoviesDetails()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            val args = Bundle()
            args.putParcelable("key", movie)

            fragment.arguments = args
            fragmentTransaction.add(R.id.fragment, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
            doOnClick(movie)
        }
    }

    companion object {
        fun newInstance() = FragmentMoviesList()
    }
}