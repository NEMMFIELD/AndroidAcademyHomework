package com.example.androidacademyhomework.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.Utils
import com.example.androidacademyhomework.database.MovieEntity
import com.example.androidacademyhomework.model.MovieDiffUtil
import xyz.hanks.library.bang.SmallBangView


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
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.view_holder_favourite, parent, false)
        return FavouriteListViewHolder(itemView)
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

    inner class FavouriteListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var imageMain: ImageView? = null
        private var titleName: TextView? = null
        private var duration: TextView? = null
        private var numbReviews: TextView? = null
        private var age: TextView? = null
        private var genre: TextView? = null
        private var rating: RatingBar? = null

        init {
            imageMain = itemView.findViewById(R.id.movie_img_favourite)
            titleName = itemView.findViewById(R.id.cinema_title_favourite)
            duration = itemView.findViewById(R.id.duration_favourite)
            numbReviews = itemView.findViewById(R.id.name_favourite)
            age = itemView.findViewById(R.id.some_id_favourite)
            genre = itemView.findViewById(R.id.tag_favourite)
            rating = itemView.findViewById(R.id.redstar_rating_favourite)
        }

        fun bind(model: MovieEntity) {
            imageMain?.load(Utils.posterUrl + model.imageUrl)
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

        }
    }
    private val RecyclerView.ViewHolder.context
        get() = this.itemView.context
}