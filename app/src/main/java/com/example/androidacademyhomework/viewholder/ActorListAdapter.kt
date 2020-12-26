package com.example.androidacademyhomework.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.data.Actor
import com.example.androidacademyhomework.data.Movie


class ActorListAdapter(private var listActors: List<Actor>) :
    RecyclerView.Adapter<ActorListAdapter.ActorListViewHolder>() {
    inner class ActorListViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.view_holder_actor, parent, false)) {
        var actorImage: ImageView? = null
         var actorName: TextView? = null

        init {
            actorImage = itemView.findViewById(R.id.actor_image)
            actorName = itemView.findViewById(R.id.actor_name)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ActorListViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ActorListViewHolder, position: Int) {
        val actors = listActors[position]
        holder.actorName?.text=actors.name
        Glide.with(holder.itemView.context).load(actors.picture).centerCrop().into(holder.actorImage!!)
    }

    override fun getItemCount(): Int {
        return listActors.size
    }
}