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
import com.example.androidacademyhomework.Utils
import com.example.androidacademyhomework.database.MovieEntity
import com.example.androidacademyhomework.databinding.ViewHolderUpcomingBinding
import com.example.androidacademyhomework.model.MovieDiffUtil
import xyz.hanks.library.bang.SmallBangView


class UpcomingListAdapter(
    private val clickListener: OnRecyclerItemClicked,
    private val likeListener: ((MovieEntity) -> Unit)
) : ListAdapter<MovieEntity, UpcomingListAdapter.UpcomingListViewHolder>(MovieDiffUtil()) {
    private var movies: MutableList<MovieEntity> = mutableListOf()
    override fun submitList(list: List<MovieEntity>?) {
        movies = list?.toMutableList() ?: ArrayList()
        super.submitList(list?.let { ArrayList(it) })
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UpcomingListAdapter.UpcomingListViewHolder {
        val binding = ViewHolderUpcomingBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return UpcomingListViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: UpcomingListAdapter.UpcomingListViewHolder,
        position: Int
    ) {
        holder.bind(movies[position])
        holder.itemView.setOnClickListener {
            clickListener.onClick(movies[position])
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    inner class UpcomingListViewHolder(private val binding: ViewHolderUpcomingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: MovieEntity) {
            binding.apply {
                movieImgUpcoming.load(Utils.posterUrl + model.imageUrl)
                cinemaTitleUpcoming.text = model.title
                durationUpcoming.text = model.runningTime.toString().plus(" MIN")
                nameUpcoming.text = model.reviewCount.toString().plus(" REVIEWS")
                if (model.pgAge) {
                    someIdUpcoming.text = "16"
                } else {
                    someIdUpcoming.text = "13"
                }
                tagUpcoming.text = model.genres?.joinToString { it }
                redstarRatingUpcoming.rating = model.rating * 0.5F
                likeHeartUpcoming.apply {
                    isSelected = model.isLiked
                    setOnClickListener {
                        isSelected = if (isSelected) {
                            false
                        } else {
                            likeAnimation()
                            true
                        }
                        model.isLiked = isSelected
                        likeListener.invoke(model)
                    }
                }
            }
        }
    }

    private val RecyclerView.ViewHolder.context
        get() = this.itemView.context
}