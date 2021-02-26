package com.example.androidacademyhomework.view.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.data.model.viewholder.CellClickListener
import com.example.androidacademyhomework.data.model.viewholder.MovieListAdapter
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
class FragmentMoviesList : Fragment(), CellClickListener {
    private lateinit var dbViewModel: DbViewModel
    private var movieListRecycler: RecyclerView? = null
    private lateinit var adapter: MovieListAdapter
    private lateinit var layoutManager: GridLayoutManager
    private var scope = CoroutineScope(Dispatchers.Main)
    private lateinit var viewModel: MovieListViewModel

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
        initViews()
        scope.launch {
            setUpMoviesListAdapter()
            setupViewModel()
            lifecycleScope.launch { viewModel.listData.collectLatest { adapter.submitData(it) } }
        }
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

    private fun initViews() {
        movieListRecycler = view?.findViewById(R.id.list_recycler_view)
    }

    private fun setUpMoviesListAdapter() {
        layoutManager = GridLayoutManager(activity, 2)
        movieListRecycler?.layoutManager = layoutManager
        adapter = MovieListAdapter(this@FragmentMoviesList)
        movieListRecycler?.setHasFixedSize(true)
        movieListRecycler?.adapter = adapter
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




