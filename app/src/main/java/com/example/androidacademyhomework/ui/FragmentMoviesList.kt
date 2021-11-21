package com.example.androidacademyhomework.ui


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
import kotlinx.serialization.ExperimentalSerializationApi


class FragmentMoviesList : Fragment() {
    private val workRepository = WorkRepository()
    private var _binding: FragmentMoviesListBinding? = null
    private val binding get() = _binding
    private val appContainer = MyApp.container

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

    override fun onDestroy() {
        movieListRecycler = null
        appContainer.workManager.cancelAllWork()
        super.onDestroy()
    }

    @SuppressLint("NotifyDataSetChanged")
    @ExperimentalSerializationApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // viewModel = ViewModelProvider(
        //    this,
        //     MovieViewModelFactory((requireActivity() as MainActivity).repository)
        // ).get(MovieViewModel::class.java)
        movieListRecycler = binding?.listRecyclerView
        movieListRecycler?.layoutManager = GridLayoutManager(context, 2)
        adapter = MovieListAdapter(clickListener = listener)
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

        viewModel.allMovies.observe(this.viewLifecycleOwner) { films ->
            films.let { adapter.submitList(it) }
        }
        appContainer.workManager.enqueue(workRepository.periodicWork)
        /* WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
             "Send data",
             ExistingPeriodicWorkPolicy.KEEP,
             workRepository.periodicWork
         )*/
        //  viewModel.insert()
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
        fun newInstance() = FragmentMoviesList()
    }
}