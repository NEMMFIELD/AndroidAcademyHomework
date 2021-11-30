package com.example.androidacademyhomework.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
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

class FragmentMoviesDetails : Fragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    private var _binding: FragmentMoviesDetailsBinding? = null
    private val binding get() = _binding
    private val appContainer = MyApp.container
    private var movieId: Long = 0
    private var day = 0
    private var month = 0
    private var year = 0
    private var hour = 0
    private var minute = 0
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private var dateAndTime = Calendar.getInstance()


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
        binding?.back?.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        return view!!
    }

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

    @ExperimentalSerializationApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val scope = CoroutineScope(Dispatchers.Main)
        super.onViewCreated(view, savedInstanceState)
        actorRecycler = binding?.actorRecyclerView
        movieId = arguments?.getLong("ID")!!
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
        binding?.calendarBtn?.setOnClickListener {
            Toast.makeText(requireContext(), "You click on calendar", Toast.LENGTH_SHORT).show()
            //showDateTimePicker()
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

    private fun getDateTimeCalendar() {
        val cal: Calendar = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }

    private fun showDateTimePicker() {
        getDateTimeCalendar()
        //Запросить разрешение, и в случае "Да" - показать
        DatePickerDialog(requireContext(), this, year, month, day).show()
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        getDateTimeCalendar()
        //Запросить разрешение, и в случае "Да" - показать
        TimePickerDialog(requireContext(), this, hour, minute, true).show()
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        Log.d("TAG", "Got the time")
        //Тут по сути надо прокинуть в реальный календарь на девайсе.
        movieId = arguments?.getLong("ID")!!
        val selectedMovie = appContainer.moviesRepository.getMovieById(movieId)
        println("The Movie is: $selectedMovie")
        scheduleMovieInCalendar(selectedMovie.title!!, dateAndTime)
    }

    fun scheduleMovieInCalendar(movieTitle: String, dateAndTime: Calendar) {

        val intent = Intent(Intent.ACTION_INSERT)
        with(intent)
        {
            type = "vnd.android.cursor.item/event"
            putExtra(CalendarContract.Events.TITLE, movieTitle)
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, dateAndTime.timeInMillis)
            putExtra(CalendarContract.Events.ALL_DAY, true)
            putExtra(CalendarContract.Events.STATUS, 1)
            putExtra(CalendarContract.Events.VISIBLE, 1)
            putExtra(CalendarContract.Events.HAS_ALARM, 1)
        }
        startActivity(intent)
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