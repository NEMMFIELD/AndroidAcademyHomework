package com.example.androidacademyhomework.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.adapter.MovieListAdapter
import com.example.androidacademyhomework.adapter.OnRecyclerItemClicked
import com.example.androidacademyhomework.data.JsonMovieRepository
import com.example.androidacademyhomework.data.MovieRepository
import com.example.androidacademyhomework.model.Model
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import kotlinx.serialization.ExperimentalSerializationApi

class FragmentMoviesList : Fragment() {
    private val scope:CoroutineScope = CoroutineScope(Dispatchers.Main + Job())
    private var movieListRecycler: RecyclerView? = null
    private lateinit var adapter: MovieListAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_movies_list, container, false)
    }

    override fun onStart() {
        super.onStart()
        scope.launch {updateData()}
    }

    override fun onDetach() {
        movieListRecycler = null
        scope.cancel()
        super.onDetach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieListRecycler = view.findViewById(R.id.list_recycler_view)
        adapter = MovieListAdapter(clickListener = listener)
        movieListRecycler?.layoutManager = GridLayoutManager(context, 2)
        movieListRecycler?.adapter = adapter
    }

    @ExperimentalSerializationApi
    @SuppressLint("NotifyDataSetChanged")
    private suspend fun updateData() {
        adapter.bindMovies(JsonMovieRepository(requireContext()).loadMovies())
        adapter.notifyDataSetChanged()
    }

    private fun doOnClick(movie: Model) {
        movieListRecycler?.let { rv ->
            Snackbar.make(
                rv,
                "You chose: {${movie.title}}",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private val listener = object : OnRecyclerItemClicked {
        override fun onClick(movie: Model) {
            val fragment: Fragment = FragmentMoviesDetails()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            val args = Bundle()
            args.putParcelable("key",movie)
            fragment.arguments = args
            fragmentTransaction.replace(R.id.fragment, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
            doOnClick(movie)
        }
    }

    companion object {
        fun newInstance() = FragmentMoviesList()
    }
}