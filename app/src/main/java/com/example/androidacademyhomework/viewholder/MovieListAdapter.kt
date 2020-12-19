package com.example.androidacademyhomework.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.data.Movie

class MovieListAdapter(private var listMovies: List<Movie>, private val cellClickListener: CellClickListener) : RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder>()
{
    inner class MovieListViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(R.layout.view_holder_movie, parent, false))
    {
        private var imageMain: ImageView? = null
        private var titleName: TextView? = null
        private var duration: TextView? = null
        private var numbReviews: TextView? = null
        private var age: ImageView? = null
        var genre: TextView? = null
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

        fun bind(movie: Movie) {
            Glide.with(itemView.context).load(listMovies[layoutPosition].poster).into(imageMain!!)
            titleName?.text=movie.title
            (movie.runtime.toString()+" MIN").also { duration?.text = it }
            numbReviews?.text=movie.numberOfRatings.toString() + " REVIEWS"
            val builder = StringBuilder()
            for (n in movie.genres) {
                builder.append(n.name+", ")
            }
            builder.deleteCharAt(builder.lastIndexOf(","));
          genre?.text= builder.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MovieListViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {
        val movieList: Movie = listMovies[position]
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

