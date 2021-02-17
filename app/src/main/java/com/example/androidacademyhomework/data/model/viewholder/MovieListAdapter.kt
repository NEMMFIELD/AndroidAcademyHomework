package com.example.androidacademyhomework.data.model.viewholder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.database.AppDatabase
import com.example.androidacademyhomework.network.API_KEY
import com.example.androidacademyhomework.network.RetrofitModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieListAdapter(
    private var listMovies: MutableList<ResultsItem>,
    private val cellClickListener: CellClickListener?
) : RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder>() {


    val scope = CoroutineScope(Dispatchers.Main)
    fun addItems(newMovies: List<ResultsItem>) {
        this.listMovies.addAll(newMovies)
        notifyItemRangeInserted(
            this.listMovies.size,
            newMovies.size - 1
        )
    }

    inner class MovieListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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

        fun bind(movie: ResultsItem) {

            scope.launch {
                val config = RetrofitModule.moviesApi.getConfig(API_KEY)
                val strUrl: String =
                    config.images?.secureBaseUrl + config.images?.posterSizes?.get(4) + movie.posterPath
                imageMain!!.load(strUrl)
                titleName?.text = movie.title
                val movieInfoRequest = RetrofitModule.moviesApi.getMovieInfo(
                    movie.id,
                    API_KEY
                )
                duration?.text = movieInfoRequest.runtime.toString().plus(" MIN")
                genre?.text = movieInfoRequest.genres?.map { it!!.name }!!.joinToString()
                stars?.rating = movie.voteAverage!! * 0.5F
                numbReviews?.text = movieInfoRequest.voteCount.toString().plus(" REVIEWS")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder {
        return MovieListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.view_holder_movie, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {
        val movieList: List<ResultsItem> = listMovies
        holder.bind(movieList[position])
        holder.itemView.setOnClickListener {
            cellClickListener?.onCellClickListener(holder.itemView, position)
        }
    }

    override fun getItemCount(): Int {
        return listMovies.size
    }
}

interface CellClickListener {
    fun onCellClickListener(view: View, position: Int)
}