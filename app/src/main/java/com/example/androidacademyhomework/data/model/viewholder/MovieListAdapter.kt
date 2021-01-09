package com.example.androidacademyhomework.data.model.viewholder

import android.util.Log
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
    private val cellClickListener: CellClickListener?
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
            scope.launch {
                val config = RetrofitModule.moviesApi.getConfig()
                val strUrl: String =
                    config.images?.secureBaseUrl + config.images?.posterSizes?.get(6) + movie.results?.get(
                        adapterPosition
                    )?.posterPath
                imageMain!!.load(strUrl)
                titleName?.text = movie.results?.get(adapterPosition)?.title
                val movieInfoRequest = RetrofitModule.moviesApi.getMovieInfo(
                    movie.results?.get(
                        index = adapterPosition
                    )?.id!!
                )
                Log.d("duration", movieInfoRequest.toString())
                duration?.text = movieInfoRequest.runtime.toString().plus(" MIN")
                genre?.text = movieInfoRequest.genres?.map { it!!.name }!!.joinToString()
                stars?.rating = movie.results[adapterPosition]?.voteAverage!! * 0.5F
                numbReviews?.text = movieInfoRequest.voteCount.toString().plus(" REVIEWS")
            }
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
            cellClickListener?.onCellClickListener(holder.itemView, position)
        }
    }

    override fun getItemCount(): Int {
        return listMovies.results!!.size
    }
}

interface CellClickListener {
    fun onCellClickListener(view: View, position: Int)
}

