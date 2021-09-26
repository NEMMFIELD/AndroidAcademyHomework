package com.example.androidacademyhomework.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.Utils.Companion.posterUrl
import com.example.androidacademyhomework.database.MovieEntity

class MovieListAdapter(
    private val clickListener: OnRecyclerItemClicked
) : RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder>() {
    private var movies: MutableList<MovieEntity> = mutableListOf()

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

        @SuppressLint("SetTextI18n")
        fun bind(model: MovieEntity) {
            imageMain?.load(posterUrl + model.imageUrl)
            titleName?.text = model.title
            duration?.text = model.runningTime.toString().plus(" MIN")
            numbReviews?.text = model.reviewCount.toString().plus(" REVIEWS")
            if (model.pgAge) {
                age?.text = "16"
            } else {
                age?.text = "13"
            }
            genre?.text = model.genres?.joinToString { it }
            if (model.isLiked) like?.setImageResource(R.drawable.ic_liked)
            else like?.setImageResource(R.drawable.ic_like)
            rating?.rating = model.rating * 0.5F
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun bindMovies(newMovies: List<MovieEntity>) {
        movies.addAll(newMovies)
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
    fun onClick(movie: MovieEntity)
}

