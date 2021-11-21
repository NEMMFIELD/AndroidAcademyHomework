package com.example.androidacademyhomework.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.Utils.Companion.posterUrl
import com.example.androidacademyhomework.database.MovieEntity
import com.example.androidacademyhomework.databinding.FragmentMoviesListBinding
import com.example.androidacademyhomework.databinding.ViewHolderMovieBinding
import com.example.androidacademyhomework.model.MovieDiffUtil
import com.example.androidacademyhomework.network.pojopack.Movie

class MovieListAdapter(
    private val clickListener: OnRecyclerItemClicked
) : ListAdapter<MovieEntity, MovieListAdapter.MovieListViewHolder>(MovieDiffUtil()) {
    private var movies: MutableList<MovieEntity> = mutableListOf()

    inner class MovieListViewHolder(private val binding: ViewHolderMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(model: MovieEntity) = with(binding)
        {
           movieImg.load(posterUrl + model.imageUrl)
           cinemaTitle.text = model.title
           duration.text = model.runningTime.toString().plus(" MIN")
            name.text = model.reviewCount.toString().plus(" REVIEWS")
            if (model.pgAge) {
                someId.text = "16"
            } else {
                someId.text = "13"
            }
           tag.text = model.genres?.joinToString { it }
            if (model.isLiked) toLike?.setImageResource(R.drawable.ic_liked)
            else toLike.setImageResource(R.drawable.ic_like)
            redstarRating.rating = model.rating * 0.5F
        }
    }


   override fun submitList(list: List<MovieEntity>?) {
       movies = list?.toMutableList() ?: ArrayList()
        super.submitList(list?.let { ArrayList(it) })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder {
        val binding = ViewHolderMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {
        holder.bind(movies[position])
        holder.itemView.setOnClickListener { clickListener.onClick(movies[position]) }
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context,R.anim.alpha)
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

