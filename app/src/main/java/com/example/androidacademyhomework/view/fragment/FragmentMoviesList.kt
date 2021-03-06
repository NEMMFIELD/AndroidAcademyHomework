package com.example.androidacademyhomework.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.data.model.viewholder.CellClickListener
import com.example.androidacademyhomework.data.model.viewholder.ClickListener
import com.example.androidacademyhomework.data.model.viewholder.MovieListAdapter
import com.example.androidacademyhomework.data.model.viewholder.ResultsItem
import com.example.androidacademyhomework.database.AppDatabase
import com.example.androidacademyhomework.database.DbViewModel
import com.example.androidacademyhomework.network.RetrofitModule
import com.example.androidacademyhomework.viewmodel.MovieListViewModel
import com.example.androidacademyhomework.viewmodel.MovieListViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@Suppress("DEPRECATION")
class FragmentMoviesList : Fragment() {
    private var listener: ClickListener? = null
    private lateinit var dbViewModel: DbViewModel
    private var movieListRecycler: RecyclerView? = null
    private lateinit var adapter: MovieListAdapter
    private lateinit var layoutManager: GridLayoutManager
    private var scope = CoroutineScope(Dispatchers.Main)
    private lateinit var viewModel: MovieListViewModel
    private var currentVisiblePosition = 0

    companion object {
        fun newInstance() = FragmentMoviesList()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dbViewModel = ViewModelProvider(this).get(DbViewModel::class.java)
        return inflater.inflate(R.layout.fragment_movies_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, MovieListViewModelFactory(RetrofitModule.moviesApi))[MovieListViewModel::class.java]
        initViews()
        setUpMoviesListAdapter()
     //   setupViewModel()

     //   lifecycleScope.launch { viewModel.listData.collectLatest { adapter.submitData(it) } }
        listener = activity as ClickListener
    }
    override fun onStart() {
        super.onStart()
      lifecycleScope.launch{viewModel.listData.collectLatest { adapter.submitData(it) }}
    }

   // private fun setupViewModel() {
     //   viewModel =
       //     ViewModelProvider(
         //       this,
           //     MovieListViewModelFactory(RetrofitModule.moviesApi)
            //)[MovieListViewModel::class.java]
    //}

    override fun onPause() {
        super.onPause()
        currentVisiblePosition = (movieListRecycler?.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
    }

    override fun onResume() {
        super.onResume()
        (movieListRecycler?.layoutManager as GridLayoutManager).scrollToPosition(currentVisiblePosition)
    }

    override fun onDestroy() {
      //  AppDatabase.destroyDataBase()
        movieListRecycler?.adapter = null
        movieListRecycler = null
        currentVisiblePosition = 0
        super.onDestroy()
    }

    private fun initViews() {
        movieListRecycler = view?.findViewById(R.id.list_recycler_view)
    }

    private fun setUpMoviesListAdapter() {
        layoutManager = GridLayoutManager(activity, 2)
        movieListRecycler?.layoutManager = layoutManager
        adapter = MovieListAdapter(movieClickListener)
        movieListRecycler?.adapter = adapter
    }

    private fun doOnClick(movie: ResultsItem) {
        listener?.changeFragment(this, movie)
    }

    private val movieClickListener = object : CellClickListener {
        override fun onCellClickListener(movie: ResultsItem) {
            doOnClick(movie)
        }
    }
        /*  private suspend fun insertDataToDatabase() {
          for (i in loadMovies().indices) {
              val movieDb = MovieDb(
                  loadMovies()[i].id,
                  RetrofitModule.moviesApi.getConfig(
                      API_KEY
                  ).images?.secureBaseUrl,
                  loadMovies()[i].posterPath,
                  loadMovies()[i].title,
                  loadMovies()[i].voteAverage,
                  listOf(
                      RetrofitModule.moviesApi.getMovieInfo(
                          loadMovies()[i].id,
                          API_KEY
                      ).genres!!.map { it!!.name }.joinToString()
                  )
              )
              dbViewModel.addMovie(movieDb)
          }
          Toast.makeText(requireContext(), "Db successfully added!", Toast.LENGTH_SHORT).show()
      }*/
}




