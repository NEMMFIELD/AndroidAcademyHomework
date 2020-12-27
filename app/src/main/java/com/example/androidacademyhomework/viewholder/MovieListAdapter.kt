package com.example.androidacademyhomework.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.data.Movie

class MovieListAdapter(
    private var listMovies: List<Movie>,
    private val cellClickListener: CellClickListener
) : RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder>() {
    inner class MovieListViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(
            inflater.inflate(
                R.layout.view_holder_movie,
                parent,
                false
            )
        ) {
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
            Glide.with(itemView.context).load(listMovies[layoutPosition].poster).into(imageMain!!)
            titleName?.text = movie.title
            builder_MIN.append(movie.runtime.toString() + " MIN")
            duration?.text = builder_MIN
            val builder_REVIEWS = StringBuilder()
            builder_REVIEWS.append(movie.numberOfRatings.toString() + " REVIEWS")
            numbReviews?.text = builder_REVIEWS
            val builder = StringBuilder()
            for (n in movie.genres) {
                builder.append(n.name + ", ")
            }
            builder.deleteCharAt(builder.lastIndexOf(","));
            genre?.text = builder.toString()
            stars?.rating=movie.ratings * 0.5F
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MovieListViewHolder(inflater, parent)
    }
    override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {
        val movieList: Movie = listMovies[position]
        holder.bind(movieList)
        val item = listMovies.get(holder.adapterPosition)
        holder.itemView.setOnClickListener {
            cellClickListener.onCellClickListener(holder.itemView, position)
        }
    }
    override fun getItemCount(): Int {
        return listMovies.size
    }
}
interface CellClickListener {
    fun onCellClickListener(view: View, position: Int)
}

