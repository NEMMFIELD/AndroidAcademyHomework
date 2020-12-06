package com.example.androidacademyhomework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidacademyhomework.model.Model
import com.example.androidacademyhomework.viewholder.CellClickListener
import com.example.androidacademyhomework.viewholder.MovieListAdapter

class FragmentMoviesList : Fragment(), CellClickListener {
    private var movieList_recycler: RecyclerView? = null

    private val listMovies = listOf(
        Model(
            R.drawable.heroes,
            "Avengers",
            "137 MIN",
            "125 REVIEWS",
            R.drawable.ic_pgg,
            "Action, Adventure, Drama",
            R.drawable.ic_like,
            R.drawable.ic_star_icon,
            R.drawable.ic_star_icon,
            R.drawable.ic_star_icon,
            R.drawable.ic_star_icon,
            R.drawable.ic_non_star
        ),
        Model(
            R.drawable.tenet,
            "Tenet",
            "97 MIN",
            "98 REVIEWS",
            R.drawable.ic_pg16,
            "Action, Sci-Fi, Thriller",
            R.drawable.ic_liked,
            R.drawable.ic_star_icon,
            R.drawable.ic_star_icon,
            R.drawable.ic_star_icon,
            R.drawable.ic_star_icon,
            R.drawable.ic_non_star
        ),
        Model(
            R.drawable.widow,
            "Black Widow",
            "102 MIN",
            "38 REVIEWS",
            R.drawable.ic_pgg,
            "Action, Adventure, Sci-Fi",
            R.drawable.ic_like,
            R.drawable.ic_star_icon,
            R.drawable.ic_star_icon,
            R.drawable.ic_star_icon,
            R.drawable.ic_star_icon,
            R.drawable.ic_star_icon
        ),
        Model(
            R.drawable.woman,
            "Wonder Woman 1984",
            "120 MIN",
            "74 REVIEWS",
            R.drawable.ic_pgg,
            "Action, Adventure, Fantasy",
            R.drawable.ic_like,
            R.drawable.ic_star_icon,
            R.drawable.ic_star_icon,
            R.drawable.ic_star_icon,
            R.drawable.ic_star_icon,
            R.drawable.ic_star_icon
        )
    )

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
        super.onViewCreated(view, savedInstanceState)
        movieList_recycler = view.findViewById(R.id.list_recycler_view)
        movieList_recycler?.apply {
            layoutManager = GridLayoutManager(activity, 2)
            adapter = MovieListAdapter(listMovies, this@FragmentMoviesList)
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





