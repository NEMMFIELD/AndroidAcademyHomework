package com.example.androidacademyhomework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidacademyhomework.model.Actor
import com.example.androidacademyhomework.viewholder.ActorListAdapter

class FragmentMoviesDetails : Fragment() {
    private var actorRecycler: RecyclerView? = null
    private var listOfActors:List<Actor> = listOf(
        Actor(R.drawable.stark, "Robert Downey Jr."),
        Actor(R.drawable.cap, "Chris Evans"),
        Actor(R.drawable.hulk, "Mark Ruffalo"),
        Actor(R.drawable.tor, "Chris Hemsworth")
    )
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.fragment_movies_details, container, false)
        val backScr: TextView = v.findViewById(R.id.back)
        backScr.setOnClickListener {
            val fragment: Fragment = FragmentMoviesList()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        return v
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actorRecycler = view.findViewById(R.id.actor_recycler_view)
        actorRecycler?.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = ActorListAdapter(listOfActors)
        }
    }
}