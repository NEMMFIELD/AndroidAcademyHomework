package com.example.androidacademyhomework.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.data.JsonMovieRepository
import com.example.androidacademyhomework.model.Model
import kotlinx.coroutines.joinAll
import kotlin.time.nanoseconds

class MovieListAdapter(
    private val clickListener: OnRecyclerItemClicked
) : RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder>() {
    private var movies: List<Model> = listOf()

    inner class MovieListViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.view_holder_movie, parent, false)) {
        private var imageMain: ImageView? = null
        private var titleName: TextView? = null
        private var duration: TextView? = null
        private var numbReviews: TextView? = null
        private var age: TextView? = null
        private var genre: TextView? = null
        private var like: ImageView? = null
        private var rating: RatingBar? = null

        init {
            imageMain = itemView.findViewById(R.id.movie_img)
            titleName = itemView.findViewById(R.id.cinema_title)
            duration = itemView.findViewById(R.id.duration)
            numbReviews = itemView.findViewById(R.id.name)
            age = itemView.findViewById(R.id.some_id)
            genre = itemView.findViewById(R.id.tag)
            like = itemView.findViewById(R.id.toLike)
            rating = itemView.findViewById(R.id.redstar_rating)
        }

        fun bind(model: Model) {
            imageMain?.load(model.imageUrl)
            titleName?.text = model.title
            duration?.text = model.runningTime.toString().plus(" MIN")
            numbReviews?.text = model.reviewCount.toString().plus(" REVIEWS")
            age?.text = model.pgAge.toString()
            genre?.text = model.genres.joinToString { it.name }
            if (model.isLiked) like?.setImageResource(R.drawable.ic_liked)
            else like?.setImageResource(R.drawable.ic_like)
            rating?.rating = model.rating.toFloat()
        }
    }

    fun bindMovies(newMovies: List<Model>) {
        movies = newMovies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MovieListViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {
        holder.bind(movies[position])
        holder.itemView.setOnClickListener { clickListener.onClick(movies[position]) }
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}

private val RecyclerView.ViewHolder.context
    get() = this.itemView.context

interface OnRecyclerItemClicked {
    fun onClick(movie: Model)
}

