package com.example.androidacademyhomework.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.model.Model

class MovieListAdapter(
    private var listMovies: List<Model>,
    private val cellClickListener: CellClickListener
) : RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder>() {
    inner class MovieListViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.view_holder_movie, parent, false)) {
        private var imageMain: ImageView? = null
        private var titleName: TextView? = null
        private var duration: TextView? = null
        private var numbReviews: TextView? = null
        private var age: ImageView? = null
        private var genre: TextView? = null
        private var like: ImageView? = null
        private var firstStar: ImageView? = null
        private var secondStar: ImageView? = null
        private var thirdStar: ImageView? = null
        private var fourthStar: ImageView? = null
        private var fifthStar: ImageView? = null
        init {
            imageMain = itemView.findViewById(R.id.movie_img)
            titleName = itemView.findViewById(R.id.cinema_title)
            duration = itemView.findViewById(R.id.duration)
            numbReviews = itemView.findViewById(R.id.name)
            age = itemView.findViewById(R.id.ageRate)
            genre = itemView.findViewById(R.id.tag)
            like = itemView.findViewById(R.id.toLike)
            firstStar = itemView.findViewById(R.id.redstar_1)
            secondStar = itemView.findViewById(R.id.redstar_2)
            thirdStar = itemView.findViewById(R.id.redstar_3)
            fourthStar = itemView.findViewById(R.id.redstar_4)
            fifthStar = itemView.findViewById(R.id.non_star)
        }
        fun bind(model: Model) {
            imageMain?.setImageResource(model.imageMain)
            titleName?.text = model.titleName
            duration?.text = model.duration
            numbReviews?.text = model.numberReviews
            age?.setImageResource(model.ageRate)
            genre?.text = model.genre
            like?.setImageResource(model.liked)
            firstStar?.setImageResource(model.star1)
            secondStar?.setImageResource(model.star2)
            thirdStar?.setImageResource(model.star3)
            fourthStar?.setImageResource(model.star4)
            fifthStar?.setImageResource(model.star5)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MovieListViewHolder(inflater, parent)
    }
    override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {
        val movieList: Model = listMovies[position]
        holder.bind(movieList)
        holder.itemView.setOnClickListener { cellClickListener.onCellClickListener() }
    }
    override fun getItemCount(): Int {
        return listMovies.size
    }
}
interface CellClickListener {
    fun onCellClickListener()
}

