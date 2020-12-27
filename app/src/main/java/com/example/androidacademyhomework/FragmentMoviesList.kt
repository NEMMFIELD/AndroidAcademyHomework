package com.example.androidacademyhomework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidacademyhomework.data.loadMovies
import com.example.androidacademyhomework.viewholder.CellClickListener
import com.example.androidacademyhomework.viewholder.MovieListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FragmentMoviesList : Fragment(), CellClickListener {
    private var movieListRecycler: RecyclerView? = null
    private val scope = CoroutineScope(Job() + Dispatchers.Main)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.fragment_movies_list, container, false)
        return v
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        scope.launch {
            super.onViewCreated(view, savedInstanceState)
            movieListRecycler = view.findViewById(R.id.list_recycler_view)
            movieListRecycler?.apply {
                layoutManager = GridLayoutManager(activity, 2)
                movieListRecycler?.setHasFixedSize(true)
                adapter = MovieListAdapter(loadMovies(requireContext()), this@FragmentMoviesList)
            }
        }
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





