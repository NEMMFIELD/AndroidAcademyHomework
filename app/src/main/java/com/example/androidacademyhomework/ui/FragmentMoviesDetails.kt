package com.example.androidacademyhomework.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.androidacademyhomework.MyApp
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.Utils
import com.example.androidacademyhomework.adapter.ActorListAdapter
import com.example.androidacademyhomework.database.MovieEntity
import com.example.androidacademyhomework.databinding.FragmentMoviesDetailsBinding
import com.example.androidacademyhomework.viewmodel.MovieViewModel
import com.example.androidacademyhomework.viewmodel.MovieViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.ExperimentalSerializationApi
import java.util.*
import android.content.ContentUris
import android.net.Uri
import android.opengl.Visibility
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.androidacademyhomework.viewmodel.SharedViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.time.ExperimentalTime


class FragmentMoviesDetails : Fragment() {
    private var _binding: FragmentMoviesDetailsBinding? = null
    private val binding get() = _binding
    private val appContainer = MyApp.container
    private var movieId: Long = 0
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    @ExperimentalSerializationApi
    private val viewModel: MovieViewModel by viewModels { MovieViewModelFactory(appContainer.moviesRepository) }
    private var actorRecycler: RecyclerView? = null
    private lateinit var adapter: ActorListAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesDetailsBinding.inflate(inflater, container, false)
        val view = binding?.root
        // viewModelShared = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        binding?.back?.setOnClickListener {
            // val data = Bundle().apply { putString("ARGUMENT_MESSAGE", "Hello from FragmentB") }
            //viewModelShared.bundleFromFragmentBToFragmentA.value = data
            requireActivity().onBackPressed()
        }
        return view!!
    }

    @ExperimentalTime
    @SuppressLint("MissingPermission")
    override fun onAttach(context: Context) {
        super.onAttach(context)
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission())
            { isGranted: Boolean ->
                if (isGranted) {
                    onWriteCalendarPermissionGranted()
                }
            }
    }

    @ExperimentalTime
    @ExperimentalSerializationApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        actorRecycler = binding?.actorRecyclerView
        movieId = arguments?.getLong("arg")!!
        println("MovieId=$movieId")
        val selectedMovie = appContainer.moviesRepository.getMovieById(movieId)
        println("Selected movie's title = ${selectedMovie.title}")
        println("Selected movie's backdrop = ${selectedMovie.detailImageUrl}")
        adapter = ActorListAdapter()
        fetchMovie(selectedMovie)
        actorRecycler?.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
        actorRecycler!!.adapter = adapter
        viewModel.actorList.observe(this.viewLifecycleOwner) { actors ->
            actors.let {
                adapter.submitList(it)
            }
        }
        viewModel.calendarIntent.observe(viewLifecycleOwner, { calendarIntent ->
            if (calendarIntent != null) {
                startActivity(calendarIntent)
                viewModel.scheduleMovieDone()
            }
        })
        binding?.calendarBtn?.setOnClickListener {
            Toast.makeText(requireContext(), "You click on calendar", Toast.LENGTH_SHORT).show()
            scheduleIntoCalendar()
        }
    }

    @SuppressLint("SetTextI18n")
    @ExperimentalSerializationApi
    private fun fetchMovie(movie: MovieEntity) = with(binding)
    {
        val circularProgressDrawable = CircularProgressDrawable(requireContext())
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        Glide.with(requireContext())
            .load(Utils.backdropUrl + movie.detailImageUrl)
            .placeholder(circularProgressDrawable)
            .apply(imageOption)
            .into(this!!.orig)
        this.afterTheD.text = movie.storyLine
        this.name.text = movie.title
        if (movie.pgAge) this.someId2.text = "16".plus("+")
        else this.someId2.text = "13".plus("+")
        this.tag.text = movie.genres?.joinToString { it }
        this.rating.stepSize = 0.5F
        this.rating.rating = movie.rating * 0.5F
        this.reviewsNumb.text = movie.reviewCount.toString().plus(" REVIEWS")
        movie.id?.let { viewModel.insertActor(it) }
    }

    @ExperimentalTime
    private fun scheduleIntoCalendar() {
        activity?.let {
            if (ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.WRITE_CALENDAR
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                onWriteCalendarPermissionGranted()
            } else {
                showPermissionExplanationDialog()
            }
        }
    }

    @ExperimentalTime
    @RequiresPermission(Manifest.permission.WRITE_CALENDAR)
    private fun onWriteCalendarPermissionGranted() {
        showDateTimePicker()
    }

    private fun requestWriteCalendarPermission() {
        context?.let {
            requestPermissionLauncher.launch(Manifest.permission.WRITE_CALENDAR)
        }
    }

    private fun showPermissionExplanationDialog() {
        context?.let {
            AlertDialog.Builder(it)
                .setMessage("Can i write in calendar?")
                .setPositiveButton("Yes") { dialog, _ ->
                    requestWriteCalendarPermission()
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    @ExperimentalTime
    private fun showDateTimePicker() {
        //Запросить разрешение, и в случае "Да" - показать
        DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                appContainer.dateAndTime.set(year, monthOfYear, dayOfMonth)
                launchTimePicker()
            },
            appContainer.dateAndTime.get(Calendar.YEAR),
            appContainer.dateAndTime.get(Calendar.MONTH),
            appContainer.dateAndTime.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    @ExperimentalTime
    private fun launchTimePicker() {
        val selectedMovie = appContainer.moviesRepository.getMovieById(movieId)
        TimePickerDialog(
            requireContext(), { _, hour, minute ->
                // fix time
                appContainer.dateAndTime.set(
                    appContainer.dateAndTime.get(Calendar.YEAR),
                    appContainer.dateAndTime.get(Calendar.MONTH),
                    appContainer.dateAndTime.get(Calendar.DAY_OF_MONTH),
                    hour,
                    minute
                )
                // start calendar intent
                viewModel.scheduleMovieInCalendar(
                    binding?.name?.text.toString(),
                    appContainer.dateAndTime,
                    requireContext(), selectedMovie)
            },
            appContainer.dateAndTime.get(Calendar.HOUR),
            appContainer.dateAndTime.get(Calendar.MINUTE),
            true
        ).show()
    }

    companion object {
        private const val ID = "ID"
        fun newInstance(movieId: Long): FragmentMoviesDetails {
            val fragment = FragmentMoviesDetails()
            val args = Bundle()
            args.putLong(ID, movieId)
            fragment.arguments = args
            return fragment
        }

        private val imageOption = RequestOptions()
            .fallback(R.drawable.err)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
    }
}