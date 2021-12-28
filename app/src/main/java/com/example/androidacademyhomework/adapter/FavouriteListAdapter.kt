package com.example.androidacademyhomework.adapter

import android.annotation.SuppressLint
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
import com.example.androidacademyhomework.Utils
import com.example.androidacademyhomework.database.MovieEntity
import com.example.androidacademyhomework.databinding.ViewHolderFavouriteBinding
import com.example.androidacademyhomework.model.MovieDiffUtil


class FavouriteListAdapter(
    private val clickListener: OnRecyclerItemClicked
) : ListAdapter<MovieEntity, FavouriteListAdapter.FavouriteListViewHolder>(MovieDiffUtil()) {
    private var movies: MutableList<MovieEntity> = mutableListOf()
    override fun submitList(list: List<MovieEntity>?) {
        movies = list?.toMutableList() ?: ArrayList()
        super.submitList(list?.let { ArrayList(it) })
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavouriteListAdapter.FavouriteListViewHolder {
        val binding = ViewHolderFavouriteBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return FavouriteListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavouriteListAdapter.FavouriteListViewHolder, position: Int) {
        holder.bind(movies[position])
        holder.itemView.setOnClickListener {
            clickListener.onClick(movies[position])
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    inner class FavouriteListViewHolder(private val binding:  ViewHolderFavouriteBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(model: MovieEntity) = with(binding)
            {
               movieImgFavourite.load(Utils.posterUrl + model.imageUrl)
               cinemaTitleFavourite.text = model.title
               durationFavourite.text = model.runningTime.toString().plus(" MIN")
               nameFavourite.text = model.reviewCount.toString().plus(" REVIEWS")
                if (model.pgAge) {
                    ageFavourite?.text = "16"
                } else {
                   ageFavourite?.text = "13"
                }
               genresFavourite?.text = model.genres?.joinToString { it }
                ratingsFavourite?.rating = model.rating * 0.5F
            }
    }
    private val RecyclerView.ViewHolder.context
        get() = this.itemView.context
}