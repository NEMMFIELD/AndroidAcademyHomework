package com.example.androidacademyhomework.ui


import android.os.Bundle
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
import com.example.androidacademyhomework.databinding.FragmentMoviesListBinding
import com.example.androidacademyhomework.viewmodel.MovieViewModel
import com.example.androidacademyhomework.viewmodel.MovieViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.internal.notifyAll

class FragmentMoviesList : Fragment() {
    private var _binding: FragmentMoviesListBinding? = null
    private val binding get() = _binding!!
    @ExperimentalSerializationApi
    private val viewModel: MovieViewModel by viewModels { MovieViewModelFactory((requireActivity() as MainActivity).repository) }
    private var movieListRecycler: RecyclerView? = null
    private lateinit var adapter: MovieListAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroy() {
       movieListRecycler = null
        // scope.cancel()
        super.onDestroy()
    }

    @ExperimentalSerializationApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // viewModel = ViewModelProvider(
        //    this,
        //     MovieViewModelFactory((requireActivity() as MainActivity).repository)
        // ).get(MovieViewModel::class.java)
        movieListRecycler = binding.listRecyclerView
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
        viewModel.allMovies.observe(this.viewLifecycleOwner){films ->
            films.let { adapter.submitList(it) }
        }
        viewModel.insert()
    }

  //  private fun updateData(movies: List<MovieEntity>) {
      // adapter.bindMovies(movies)
   //     adapter.submitList(movies)
   // }

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