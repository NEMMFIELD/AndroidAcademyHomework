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
import com.example.androidacademyhomework.adapter.OnRecyclerItemClicked
import com.example.androidacademyhomework.adapter.PopularListAdapter
import com.example.androidacademyhomework.database.MovieEntity
import com.example.androidacademyhomework.databinding.FragmentTopMoviesBinding
import com.example.androidacademyhomework.viewmodel.TopViewModel
import com.example.androidacademyhomework.viewmodel.TopViewModelFactory
import kotlinx.serialization.ExperimentalSerializationApi as ExperimentalSerializationApi1


class TopMovies : Fragment() {
    private var _binding: FragmentTopMoviesBinding? = null
    private val binding get() = _binding
    private val appContainer = MyApp.container
    @OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
    private val viewTopModel: TopViewModel by viewModels { TopViewModelFactory(appContainer.moviesRepository) }
    private var topListRecycler: RecyclerView? = null
    private lateinit var adapter: PopularListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTopMoviesBinding.inflate(inflater, container, false)
        val view = binding?.root
        return view!!
    }

    @OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        topListRecycler = binding?.listRecyclerViewTop
        val spanCount =
            if (activity?.resources?.configuration?.orientation != Configuration.ORIENTATION_PORTRAIT) {
                4
            } else {
                2
            }
        topListRecycler?.layoutManager = GridLayoutManager(context, spanCount)
        adapter = PopularListAdapter(
            clickListener = listener
        ) { movieEntity -> viewTopModel.updateLike(movieEntity) }
        topListRecycler?.adapter = adapter
       topListRecycler?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                when {
                    !recyclerView.canScrollVertically(1) -> { //1 for down
                        viewTopModel.loadMore(path = "top_rated", type = "top_rated")
                    }
                }
            }
        })
        viewTopModel.topAllMovies.observe(this.viewLifecycleOwner) { films ->
            films.let { adapter.submitList(it) }
        }
        viewTopModel.insert(path = "top_rated", type = "top_rated")
    }

    private val listener = object : OnRecyclerItemClicked {
        override fun onClick(movie: MovieEntity) {
            val bundle = Bundle()
            movie.id?.let { bundle.putLong("arg", it) }
            view?.let {
                Navigation.findNavController(it)
                    .navigate(R.id.action_topMovies_to_fragmentMoviesDetails, bundle)
            }
        }
    }
}