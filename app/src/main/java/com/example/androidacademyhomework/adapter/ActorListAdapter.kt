package com.example.androidacademyhomework.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.Utils.Companion.actorUrl
import com.example.androidacademyhomework.database.ActorsEntity
import com.example.androidacademyhomework.database.MovieEntity
import com.example.androidacademyhomework.network.pojopack.CastItem

class ActorListAdapter(private var listActors: List<ActorsEntity>) :
    RecyclerView.Adapter<ActorListAdapter.ActorListViewHolder>() {
    inner class ActorListViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.view_holder_actor, parent, false)) {
        private var actorImage: ImageView? = null
        private var actorName: TextView? = null

        init {
            actorImage = itemView.findViewById(R.id.actor_image)
            actorName = itemView.findViewById(R.id.actor_name)
        }

        fun bind(actor: ActorsEntity) {
            actorImage?.load(actorUrl + actor.profilePath)
            actorName?.text = actor.name
        }
    }
    fun bindActors(newActors: List<ActorsEntity>) {
       listActors = newActors
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ActorListViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ActorListViewHolder, position: Int) {
        val actors: ActorsEntity = listActors[position]
        holder.bind(actors)
    }

    override fun getItemCount(): Int {
        return listActors.size
    }
}