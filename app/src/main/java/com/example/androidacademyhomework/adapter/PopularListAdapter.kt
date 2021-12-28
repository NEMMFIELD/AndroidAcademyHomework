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
import com.example.androidacademyhomework.databinding.ViewHolderPopularBinding
import com.example.androidacademyhomework.model.MovieDiffUtil
import xyz.hanks.library.bang.SmallBangView


class PopularListAdapter (
    private val clickListener: OnRecyclerItemClicked,private val likeListener: ((MovieEntity) -> Unit)
) : ListAdapter<MovieEntity, PopularListAdapter.PopularListViewHolder>(MovieDiffUtil()) {
    private var movies: MutableList<MovieEntity> = mutableListOf()
    override fun submitList(list: List<MovieEntity>?) {
        movies = list?.toMutableList() ?: ArrayList()
        super.submitList(list?.let { ArrayList(it) })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularListViewHolder {
        val binding = ViewHolderPopularBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return PopularListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopularListViewHolder, position: Int) {
        holder.bind(movies[position])
        holder.itemView.setOnClickListener {
            clickListener.onClick(movies[position])
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    inner class PopularListViewHolder(private val binding: ViewHolderPopularBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: MovieEntity) = with(binding)
        {

           movieImgPopular.load(Utils.posterUrl + model.imageUrl)
           cinemaTitlePopular.text = model.title
           durationPopular.text = model.runningTime.toString().plus(" MIN")
           namePopular.text = model.reviewCount.toString().plus(" REVIEWS")
            if (model.pgAge) {
              agePopular?.text = "16"
            } else {
               agePopular?.text = "13"
            }
            genresPopular?.text = model.genres?.joinToString { it }
           ratingsPopular?.rating = model.rating * 0.5F
           likeHeartPopular.apply {
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


