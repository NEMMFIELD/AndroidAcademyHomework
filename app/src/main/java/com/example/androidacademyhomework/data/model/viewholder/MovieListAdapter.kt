package com.example.androidacademyhomework.data.model.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.network.RetrofitModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieListAdapter(
    private var listMovies: Movie,
    private val cellClickListener: CellClickListener
) : RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder>() {
    val scope = CoroutineScope(Dispatchers.Main)

    inner class MovieListViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.view_holder_movie, parent, false)) {
        private var imageMain: ImageView? = null
        private var titleName: TextView? = null
        private var duration: TextView? = null
        private var numbReviews: TextView? = null
        private var age: ImageView? = null
        var genre: TextView? = null
        private var like: ImageView? = null
        private var stars: RatingBar? = null

        init {
            imageMain = itemView.findViewById(R.id.movie_img)
            titleName = itemView.findViewById(R.id.cinema_title)
            duration = itemView.findViewById(R.id.duration)
            numbReviews = itemView.findViewById(R.id.name)
            age = itemView.findViewById(R.id.ageRate)
            genre = itemView.findViewById(R.id.tag)
            like = itemView.findViewById(R.id.toLike)
            stars = itemView.findViewById(R.id.rating)
        }

        fun bind(movie: Movie) {
            val builder_MIN = StringBuilder()
            scope.launch {
                val config = RetrofitModule.moviesApi.getConfig()
                val str: String = config.images?.secureBaseUrl + config.images?.posterSizes?.get(6) + movie.results?.get(adapterPosition)?.posterPath
                imageMain!!.load(str)
            }
            titleName?.text = movie.results?.get(adapterPosition)?.title
            builder_MIN.append(MovieInfo().runtime.toString() + " MIN")
            duration?.text = builder_MIN
            //val builder_REVIEWS = StringBuilder()
            //builder_REVIEWS.append(movie.numberOfRatings.toString() + " REVIEWS")
            //numbReviews?.text = builder_REVIEWS
            val builder = StringBuilder()
            for (name in movie.results?.get(adapterPosition)?.genreIds!!) {
                builder.append(name.toString() + ", ")
            }
            builder.deleteCharAt(builder.lastIndexOf(","));
            genre?.text = movie.results[adapterPosition]?.genreIds.toString()
            stars?.rating = movie.results[adapterPosition]?.voteAverage!! * 0.5F
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MovieListViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {
        val movieList: Movie = listMovies
        holder.bind(movieList)
        val item = listMovies
        holder.itemView.setOnClickListener {
            cellClickListener.onCellClickListener(holder.itemView, position)
        }
    }

    override fun getItemCount(): Int {
        return listMovies.results!!.size
    }
}

interface CellClickListener {
    fun onCellClickListener(view: View, position: Int)
}

