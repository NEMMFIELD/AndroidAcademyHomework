package com.example.androidacademyhomework.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.data.Movie
import com.example.androidacademyhomework.model.Actor

class ActorListAdapter(private var listActors: List<Movie>) :
    RecyclerView.Adapter<ActorListAdapter.ActorListViewHolder>()
{
    inner class ActorListViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(R.layout.view_holder_actor, parent, false))
    {
        private var actorImage: ImageView? = null
        private var actorName: TextView? = null

        init {
            actorImage = itemView.findViewById(R.id.actor_image)
            actorName = itemView.findViewById(R.id.actor_name)
        }
        fun bind(actor: Movie) {
            for (i in actor.actors){
                Glide.with(itemView.context).load(i.picture).into(actorImage!!)
                actorName?.text=i.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ActorListViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ActorListViewHolder, position: Int) {
        val actors: Movie = listActors[position]
        holder.bind(actors)
    }

    override fun getItemCount(): Int {
        return listActors.size
    }
}