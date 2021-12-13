package com.example.androidacademyhomework.ui

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
import com.example.androidacademyhomework.adapter.OnRecyclerItemClicked
import com.example.androidacademyhomework.adapter.PopularListAdapter
import com.example.androidacademyhomework.database.MovieEntity
import com.example.androidacademyhomework.databinding.FragmentTopMoviesBinding
import com.example.androidacademyhomework.viewmodel.TopViewModel
import com.example.androidacademyhomework.viewmodel.TopViewModelFactory


class TopMovies : Fragment() {
    private var _binding: FragmentTopMoviesBinding? = null
    private val binding get() = _binding
    private val appContainer = MyApp.container
    private val viewPopularModel: TopViewModel by viewModels { TopViewModelFactory(appContainer.moviesRepository) }
    private var popularListRecycler: RecyclerView? = null
    private lateinit var adapter: PopularListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTopMoviesBinding.inflate(inflater, container, false)
        val view = binding?.root
        return view!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        popularListRecycler = binding?.listRecyclerViewTop
        popularListRecycler?.layoutManager = GridLayoutManager(context, 2)
        adapter = PopularListAdapter(
            clickListener = listener
        ) { movieEntity -> viewPopularModel.updateLike(movieEntity) }
        popularListRecycler?.adapter = adapter
        popularListRecycler?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                when {
                    !recyclerView.canScrollVertically(1) -> { //1 for down
                        viewPopularModel.loadMore(path = "popular", type = "popular")
                    }
                }
            }
        })
        viewPopularModel.topAllMovies.observe(this.viewLifecycleOwner) { films ->
            films.let { adapter.submitList(it) }
        }
        viewPopularModel.insert(path = "popular", type = "popular")
    }

    private val listener = object : OnRecyclerItemClicked {
        override fun onClick(movie: MovieEntity) {

        }
    }
}