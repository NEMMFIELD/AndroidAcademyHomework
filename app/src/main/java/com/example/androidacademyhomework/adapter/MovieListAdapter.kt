package com.example.androidacademyhomework.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.Utils.Companion.posterUrl
import com.example.androidacademyhomework.database.MovieEntity
import com.example.androidacademyhomework.databinding.ViewHolderMovieBinding
import com.example.androidacademyhomework.model.MovieDiffUtil
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
      val binding = ViewHolderMovieBinding
          .inflate(LayoutInflater.from(parent.context),parent,false)
        return MovieListViewHolder(binding)
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

    inner class MovieListViewHolder(val binding:ViewHolderMovieBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: MovieEntity) {
            binding.apply {
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
            redstarRating.rating = model.rating * 0.5F
                likeHeart.apply {
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
}

private val RecyclerView.ViewHolder.context
    get() = this.itemView.context

interface OnRecyclerItemClicked {
    fun onClick(movie: MovieEntity)
}




