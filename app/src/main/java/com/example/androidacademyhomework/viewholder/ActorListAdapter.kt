package com.example.androidacademyhomework.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.model.Actor
import com.example.androidacademyhomework.model.Model

class ActorListAdapter (private var listActors:List<Actor>):RecyclerView.Adapter<ActorListAdapter.ActorListViewHolder>()
{
    inner class ActorListViewHolder(inflater:LayoutInflater,parent: ViewGroup):RecyclerView.ViewHolder(inflater.inflate(R.layout.view_holder_actor,parent,false))
    {
        private var actorImage:ImageView?=null
        private var actorName: TextView?=null
        init {

            actorImage=itemView.findViewById(R.id.actor_image)
            actorName=itemView.findViewById(R.id.actor_name)
        }


        fun bind(actor:Actor)
        {
            actorImage?.setImageResource(actor.imageActor)
            actorName?.text=actor.name
        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorListViewHolder {
        val inflater=LayoutInflater.from(parent.context)
        return ActorListViewHolder(inflater,parent)
    }

    override fun onBindViewHolder(holder: ActorListViewHolder, position: Int) {
        val actors: Actor =listActors[position]
        holder.bind(actors)
    }

    override fun getItemCount(): Int {
    return listActors.size
    }

}