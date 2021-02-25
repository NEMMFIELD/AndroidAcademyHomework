package com.example.androidacademyhomework.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidacademyhomework.R

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

@Suppress("UNCHECKED_CAST")
class FragmentMoviesList : Fragment() {
    private lateinit var dbViewModel: DbViewModel
    private var movieListRecycler: RecyclerView? = null
    private lateinit var adapter: MovieListAdapter
    private var isLoading: Boolean = false
    private lateinit var layoutManager: GridLayoutManager
    private var scope = CoroutineScope(Dispatchers.Main)
   // private val viewModel: MovieListViewModel by viewModels {
     //   MovieListViewModelFactory(
       // )
    //}
   lateinit var viewModel: MovieListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dbViewModel = ViewModelProvider(this).get(DbViewModel::class.java)
      //scope.launch { insertDataToDatabase() }
        return inflater.inflate(R.layout.fragment_movies_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        scope.launch { setUpMoviesListAdapter()
        setupViewModel()
        lifecycleScope.launch{viewModel.listData.collectLatest { adapter.submitData(it) }}}
       /* movieListRecycler?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                          //  loadMovies()
                        viewModel.movieList.value!!
                        )
                      //  insertDataToDatabase()
                        isLoading = false
                    }
                }
            }
        })*/
       // viewModel.movieList.observe(this.viewLifecycleOwner, this::updateAdapter)


    }

    private fun setupViewModel() {
        viewModel =
            ViewModelProvider(
                this,
                MovieListViewModelFactory(RetrofitModule.moviesApi)
            )[MovieListViewModel::class.java]
    }

    override fun onDestroy() {
        super.onDestroy()
        AppDatabase.destroyDataBase()
        movieListRecycler?.adapter = null
        movieListRecycler = null
    }

    private fun updateAdapter(movies: List<ResultsItem>) {
        (movieListRecycler?.adapter as? MovieListAdapter)?.apply {

        }
    }

    private fun initViews() {
        movieListRecycler = view?.findViewById(R.id.list_recycler_view)
    }

    private fun setUpMoviesListAdapter() {
        layoutManager = GridLayoutManager(activity, 2)
        movieListRecycler?.layoutManager = layoutManager
        adapter = MovieListAdapter(
        )
        movieListRecycler?.setHasFixedSize(true)
        movieListRecycler?.adapter = adapter
    }

   /* private suspend fun loadMovies(): MutableList<ResultsItem> {
        return RetrofitModule.moviesApi.getNowPlaying(
            API_KEY,
            LANGUAGE,
            PAGE_NUMB
        ).results as MutableList<ResultsItem>
    }*/

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




