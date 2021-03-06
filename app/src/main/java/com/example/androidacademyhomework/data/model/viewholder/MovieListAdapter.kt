package com.example.androidacademyhomework.data.model.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.network.API_KEY
import com.example.androidacademyhomework.network.RetrofitModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieListAdapter( private val cellClickListener: CellClickListener?) :
    PagingDataAdapter<ResultsItem, MovieListAdapter.MovieViewHolder>(MovieComparator) {

    val scope = CoroutineScope(Dispatchers.Main)


    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.titleName?.text = getItem(position)?.title
        scope.launch {
            val config = RetrofitModule.moviesApi.getConfig(API_KEY)
            val strUrl: String =
                config.images?.secureBaseUrl + config.images?.posterSizes?.get(5) + getItem(position)?.posterPath
            holder.imageMain?.load(strUrl)
            val movieInfoRequest =
                getItem(position)?.id?.let { RetrofitModule.moviesApi.getMovieInfo(it, API_KEY) }
            holder.duration?.text = movieInfoRequest?.runtime.toString().plus(" MIN")
            holder.genre?.text = movieInfoRequest?.genres?.map { it!!.name }!!.joinToString()
            holder.stars?.rating = getItem(position)?.voteAverage!! * 0.5F
            holder.numbReviews?.text = movieInfoRequest.voteCount.toString().plus(" REVIEWS")
        }

        holder.itemView.setOnClickListener {
           cellClickListener?.onCellClickListener(getItem(position)!!)
        }
    }

    inner class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageMain: ImageView? = null
        var titleName: TextView? = null
        var duration: TextView? = null
        var numbReviews: TextView? = null
        var age: ImageView? = null
        var genre: TextView? = null
        var like: ImageView? = null
        var stars: RatingBar? = null

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

        //  fun bind(data: ResultsItem, clickListener: (ResultsItem) -> Unit) {
        //      itemView.setOnClickListener { clickListener(data) }
        //  }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_holder_movie, parent, false)
        )
    }

    object MovieComparator : DiffUtil.ItemCallback<ResultsItem>() {
        override fun areItemsTheSame(oldItem: ResultsItem, newItem: ResultsItem): Boolean {
            //Id is unique.
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ResultsItem, newItem: ResultsItem): Boolean {
            return oldItem == newItem
        }
    }
}

interface CellClickListener {
    fun onCellClickListener(movie:ResultsItem)
}