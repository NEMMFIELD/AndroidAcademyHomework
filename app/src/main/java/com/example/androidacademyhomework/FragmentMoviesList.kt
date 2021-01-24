package com.example.androidacademyhomework

import android.graphics.Movie
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
    private var movieListRecycler: RecyclerView? = null
    private val listMovies:List<Model> = listOf(
        Model(
            imageMain = R.drawable.heroes,
            titleName = "Avengers",
            duration = "137 MIN",
            numberReviews = "125 REVIEWS",
            ageRate = R.drawable.ic_pgg,
            genre = "Action, Adventure, Drama",
            liked = R.drawable.ic_like,
            star1 = R.drawable.ic_star_icon,
            star2 = R.drawable.ic_star_icon,
            star3 = R.drawable.ic_star_icon,
            star4 = R.drawable.ic_star_icon,
            star5 = R.drawable.ic_non_star
        ),
        Model(
            imageMain = R.drawable.tenet,
            titleName = "Tenet",
            duration = "97 MIN",
            numberReviews = "98 REVIEWS",
            ageRate = R.drawable.ic_pg16,
            genre = "Action, Sci-Fi, Thriller",
            liked = R.drawable.ic_liked,
            star1 = R.drawable.ic_star_icon,
            star2 = R.drawable.ic_star_icon,
            star3 = R.drawable.ic_star_icon,
            star4 = R.drawable.ic_star_icon,
            star5 = R.drawable.ic_non_star
        ),
        Model(
            imageMain = R.drawable.widow,
            titleName = "Black Widow",
            duration = "102 MIN",
            numberReviews = "38 REVIEWS",
            ageRate = R.drawable.ic_pgg,
            genre = "Action, Adventure, Sci-Fi",
            liked = R.drawable.ic_like,
            star1 = R.drawable.ic_star_icon,
            star2 = R.drawable.ic_star_icon,
            star3 = R.drawable.ic_star_icon,
            star4 = R.drawable.ic_star_icon,
            star5 = R.drawable.ic_star_icon
        ),
        Model(
            imageMain = R.drawable.woman,
            titleName = "Wonder Woman 1984",
            duration = "120 MIN",
            numberReviews = "74 REVIEWS",
            ageRate = R.drawable.ic_pgg,
            genre = "Action, Adventure, Fantasy",
            liked = R.drawable.ic_like,
            star1 = R.drawable.ic_star_icon,
            star2 = R.drawable.ic_star_icon,
            star3 = R.drawable.ic_star_icon,
            star4 = R.drawable.ic_star_icon,
            star5 = R.drawable.ic_star_icon
        )
    )
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.fragment_movies_list, container, false)
        return v
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieListRecycler = view.findViewById(R.id.list_recycler_view)
        movieListRecycler?.apply {
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