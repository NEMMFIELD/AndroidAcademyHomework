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
    private var movieList_recycler: RecyclerView? = null
    private val scope = CoroutineScope(Job() + Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.fragment_movies_list, container, false)
        // val nextScr: ImageView = v.findViewById(R.id.test_click)
        // nextScr.setOnClickListener { view ->
        //   Navigation.findNavController(view).navigate(R.id.navigateToSecond)
        // }
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        scope.launch {
            super.onViewCreated(view, savedInstanceState)
            movieList_recycler = view.findViewById(R.id.list_recycler_view)
            movieList_recycler?.apply {
                layoutManager = GridLayoutManager(activity, 2)
                adapter = MovieListAdapter(loadMovies(requireContext()), this@FragmentMoviesList)
            }
        }
    }

    override fun onCellClickListener() {

        val fragment: Fragment = FragmentMoviesDetails()
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}





