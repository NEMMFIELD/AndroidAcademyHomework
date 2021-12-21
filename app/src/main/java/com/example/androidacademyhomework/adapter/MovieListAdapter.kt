package com.example.androidacademyhomework.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.Utils.Companion.posterUrl
import com.example.androidacademyhomework.database.MovieEntity
import com.example.androidacademyhomework.databinding.ViewHolderMovieBinding
import com.example.androidacademyhomework.model.MovieDiffUtil
import com.example.androidacademyhomework.network.pojopack.Movie
import xyz.hanks.library.bang.SmallBangView


class MovieListAdapter(
    private val clickListener: OnRecyclerItemClicked,private val likeListener: ((MovieEntity) -> Unit)
) : ListAdapter<MovieEntity, MovieListAdapter.MovieListViewHolder>(MovieDiffUtil()) {
    private var movies: MutableList<MovieEntity> = mutableListOf()
    override fun submitList(list: List<MovieEntity>?) {
        movies = list?.toMutableList() ?: ArrayList()
        super.submitList(list?.let { ArrayList(it) })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_holder_movie, parent, false)

        return MovieListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {
        holder.bind(movies[position])
        holder.itemView.setOnClickListener {
            clickListener.onClick(movies[position])
        }

    }

    override fun getItemCount(): Int {
        return movies.size
    }

    inner class MovieListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var imageMain: ImageView? = null
        private var titleName: TextView? = null
        private var duration: TextView? = null
        private var numbReviews: TextView? = null
        private var age: TextView? = null
        private var genre: TextView? = null
        private var like: SmallBangView? = null
        private var rating: RatingBar? = null

        init {
            imageMain = itemView.findViewById(R.id.movie_img)
            titleName = itemView.findViewById(R.id.cinema_title)
            duration = itemView.findViewById(R.id.duration)
            numbReviews = itemView.findViewById(R.id.name)
            age = itemView.findViewById(R.id.some_id)
            genre = itemView.findViewById(R.id.tag)
            like = itemView.findViewById(R.id.like_heart)
            rating = itemView.findViewById(R.id.redstar_rating)
        }

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
            rating?.rating = model.rating * 0.5F
                like?.apply {
                    isSelected = model.isLiked
                    setOnClickListener {
                        isSelected = if (isSelected) {
                            false
                        } else {
                            likeAnimation()
                            true
                        }
                        model.isLiked = isSelected
                        likeListener.apply{invoke(model)}
                    }
            }
        }
    }
}

private val RecyclerView.ViewHolder.context
    get() = this.itemView.context

interface OnRecyclerItemClicked {
    fun onClick(movie: MovieEntity)
}




