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
import com.example.androidacademyhomework.databinding.ViewHolderTopBinding
import com.example.androidacademyhomework.model.MovieDiffUtil
import xyz.hanks.library.bang.SmallBangView


class TopListAdapter(
    private val clickListener: OnRecyclerItemClicked,
    private val likeListener: ((MovieEntity) -> Unit)
) : ListAdapter<MovieEntity, TopListAdapter.TopListViewHolder>(MovieDiffUtil()) {
    private var movies: MutableList<MovieEntity> = mutableListOf()
    override fun submitList(list: List<MovieEntity>?) {
        movies = list?.toMutableList() ?: ArrayList()
        super.submitList(list?.let { ArrayList(it) })
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TopListAdapter.TopListViewHolder {
        val binding = ViewHolderTopBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return TopListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TopListAdapter.TopListViewHolder, position: Int) {
        holder.bind(movies[position])
        holder.itemView.setOnClickListener {
            clickListener.onClick(movies[position])
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    inner class TopListViewHolder(private val binding: ViewHolderTopBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: MovieEntity) = with (binding){

          movieImgTop.load(Utils.posterUrl + model.imageUrl)
            cinemaTitleTop.text = model.title
            durationTop.text = model.runningTime.toString().plus(" MIN")
            nameTop.text = model.reviewCount.toString().plus(" REVIEWS")
            if (model.pgAge) {
                ageTop?.text = "16"
            } else {
                ageTop?.text = "13"
            }
           genresTop?.text = model.genres?.joinToString { it }
           ratingsTop?.rating = model.rating * 0.5F
            likeHeartTop.apply {
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

    private val RecyclerView.ViewHolder.context
        get() = this.itemView.context
}