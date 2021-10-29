package com.example.androidacademyhomework.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.androidacademyhomework.MainActivity
import com.example.androidacademyhomework.Utils
import com.example.androidacademyhomework.adapter.ActorListAdapter
import com.example.androidacademyhomework.adapter.MovieListAdapter
import com.example.androidacademyhomework.database.ActorsEntity
import com.example.androidacademyhomework.database.MovieEntity
import com.example.androidacademyhomework.databinding.FragmentMoviesDetailsBinding
import com.example.androidacademyhomework.network.NetworkConnection
import com.example.androidacademyhomework.network.pojopack.CastItem
import com.example.androidacademyhomework.viewmodel.ActorsViewModel
import com.example.androidacademyhomework.viewmodel.ActorsViewModelFactory
import com.example.androidacademyhomework.viewmodel.MovieViewModel
import com.example.androidacademyhomework.viewmodel.MovieViewModelFactory
import kotlinx.serialization.ExperimentalSerializationApi

class FragmentMoviesDetails : Fragment() {
    private var _binding: FragmentMoviesDetailsBinding? = null
    private val binding get() = _binding!!

    @ExperimentalSerializationApi
    // private val actorsViewModel: ActorsViewModel by viewModels { ActorsViewModelFactory((requireActivity() as MainActivity).repository) }
    private val viewModel: MovieViewModel by viewModels { MovieViewModelFactory((requireActivity() as MainActivity).repository) }
    private var actorRecycler: RecyclerView? = null
    private lateinit var adapter: ActorListAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.back.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        return view
    }

    @ExperimentalSerializationApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actorRecycler = binding.actorRecyclerView
        val list = arguments?.getParcelable<MovieEntity>("key")
        adapter = ActorListAdapter()
        actorRecycler?.apply {
            layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            // adapter = viewModel.actorList.value!!.let { ActorListAdapter(it) }
        }
        actorRecycler!!.adapter = adapter
        val networkConnection = NetworkConnection(requireContext())
        networkConnection.observe(viewLifecycleOwner, Observer { isConnected ->
            if (isConnected) {
                if (list != null) {
                    fetchMovie(list)
                }
                viewModel.actorList.observe(this.viewLifecycleOwner) { actors ->
                    actors.let { adapter.submitList(it) }
                }
            } else {
                viewModel.actorList.observe(this.viewLifecycleOwner) { actors ->
                    actors.let { adapter.submitList(it) }
                }
                Toast.makeText(requireContext(), "Turn on internet", Toast.LENGTH_LONG).show()
            }
        })
        /* viewModel.actorList.observe(this.viewLifecycleOwner){actors->
             actors.let { adapter.submitList(it) }
         }*/
    }

    @ExperimentalSerializationApi
    private fun fetchMovie(movie: MovieEntity) {
        binding.orig.load(Utils.backdropUrl + movie.detailImageUrl)
        binding.afterTheD.text = movie.storyLine
        binding.name.text = movie.title
        if (movie.pgAge) binding.someId2.text = "16".plus("+")
        else binding.someId2.text = "13".plus("+")
        binding.tag.text = movie.genres?.joinToString { it }
        binding.rating.stepSize = 0.5F
        binding.rating.rating = movie.rating * 0.5F
        binding.reviewsNumb.text = movie.reviewCount.toString().plus(" REVIEWS")

        /*  movie.id?.let { actorsViewModel.insertActor(it) }
          actorsViewModel.allActors.observe(this.viewLifecycleOwner){actors->
              actors.let { adapter.bindActors(it!!) }
          }*/
        movie.id?.let { viewModel.insertActor(it) }
    }
}