package com.example.androidacademyhomework.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.model.Model

class MovieListAdapter(private var listMovies:List<Model>):RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder>()
{
    inner class MovieListViewHolder(inflater:LayoutInflater,parent:ViewGroup):RecyclerView.ViewHolder(inflater.inflate(R.layout.view_holder_movie,parent,false))
    {
        private var imageMain:ImageView?=null
        private var titleName: TextView?=null
        private var duration:TextView?=null
        private var numbReviews:TextView?=null
        private var age:ImageView?=null
        private var genre:TextView?=null
        private var like:ImageView?=null


        init
        {
            imageMain=itemView.findViewById(R.id.movie_img)
            titleName=itemView.findViewById(R.id.cinema_title)
            duration=itemView.findViewById(R.id.duration)
            numbReviews=itemView.findViewById(R.id.name)
            age=itemView.findViewById(R.id.ageRate)
            genre=itemView.findViewById(R.id.tag)
            like=itemView.findViewById(R.id.toLike)
        }

        fun bind(model:Model)
        {
            imageMain?.setImageResource(model.image_Main)
            titleName?.text=model.title_name
            duration?.text=model.duration
            numbReviews?.text=model.number_reviews
            age?.setImageResource(model.age_rate)
            genre?.text=model.genre
            like?.setImageResource(model.liked)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder {
        val inflater=LayoutInflater.from(parent.context)
        return MovieListViewHolder(inflater,parent)
    }

    override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {
        val movieList:Model=listMovies[position]
        holder.bind(movieList)
    }

    override fun getItemCount(): Int {
        return listMovies.size
    }

}

