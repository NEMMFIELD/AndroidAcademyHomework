package com.example.androidacademyhomework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidacademyhomework.model.Model
import com.example.androidacademyhomework.viewholder.MovieListAdapter

class FragmentMoviesList : Fragment() {
private var movieList_recycler:RecyclerView?=null
private val listMovies= listOf(
Model(R.drawable.heroes,"Avengers","137 MIN","125 REVIEWS",R.drawable.ic_pgg,"Action, Adventure, Drama",R.drawable.ic_like),
        Model(R.drawable.heroes,"Avengers","137 MIN","125 REVIEWS",R.drawable.ic_pgg,"Action, Adventure, Drama",R.drawable.ic_like),
    Model(R.drawable.heroes,"Avengers","137 MIN","125 REVIEWS",R.drawable.ic_pgg,"Action, Adventure, Drama",R.drawable.ic_like),
    Model(R.drawable.heroes,"Avengers","137 MIN","125 REVIEWS",R.drawable.ic_pgg,"Action, Adventure, Drama",R.drawable.ic_like)
)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.fragment_movies_list, container, false)
       // val nextScr: ImageView = v.findViewById(R.id.movie_img)
       // nextScr.setOnClickListener { view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.navigateToSecond) } }
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
movieList_recycler=view.findViewById(R.id.list_recycler_view)
movieList_recycler?.apply {
    layoutManager=GridLayoutManager(activity,2)
    adapter= MovieListAdapter(listMovies)
}
    }

}
