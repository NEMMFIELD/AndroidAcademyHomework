package com.example.androidacademyhomework.data.model.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.androidacademyhomework.R

/*class ActorListAdapter(private var listActors: List<Actor>) :
    RecyclerView.Adapter<ActorListAdapter.ActorListViewHolder>() {
    fun bindActors(newActors: List<Actor>) {
        listActors = newActors
        notifyDataSetChanged()
    }

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
      //  holder.actorName?.text = actors.name
       // holder.actorImage!!.load(actors.picture)
    }

    override fun getItemCount(): Int {
        return listActors.size
    }
}*/