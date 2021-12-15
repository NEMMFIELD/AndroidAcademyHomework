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
import com.example.androidacademyhomework.Utils
import com.example.androidacademyhomework.database.MovieEntity
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
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.view_holder_upcoming, parent, false)
        return UpcomingListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UpcomingListAdapter.UpcomingListViewHolder, position: Int) {
        holder.bind(movies[position])
        holder.itemView.setOnClickListener {
            clickListener.onClick(movies[position])
        }
        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.alpha)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    inner class UpcomingListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var imageMain: ImageView? = null
        private var titleName: TextView? = null
        private var duration: TextView? = null
        private var numbReviews: TextView? = null
        private var age: TextView? = null
        private var genre: TextView? = null
        private var like: SmallBangView? = null
        private var rating: RatingBar? = null

        init {
            imageMain = itemView.findViewById(R.id.movie_img_upcoming)
            titleName = itemView.findViewById(R.id.cinema_title_upcoming)
            duration = itemView.findViewById(R.id.duration_upcoming)
            numbReviews = itemView.findViewById(R.id.name_upcoming)
            age = itemView.findViewById(R.id.some_id_upcoming)
            genre = itemView.findViewById(R.id.tag_upcoming)
            like = itemView.findViewById(R.id.like_heart_upcoming)
            rating = itemView.findViewById(R.id.redstar_rating_upcoming)
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
                    likeListener.invoke(model)
                }
            }
        }
    }

    private val RecyclerView.ViewHolder.context
        get() = this.itemView.context
}