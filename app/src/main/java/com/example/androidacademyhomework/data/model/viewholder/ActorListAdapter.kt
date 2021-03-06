package com.example.androidacademyhomework.data.model.viewholder

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.network.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActorListAdapter() :
    RecyclerView.Adapter<ActorListAdapter.ActorListViewHolder>() {
    private var listActors: List<CastItem?>? = listOf()
    val scope = CoroutineScope(Dispatchers.Main)
    fun bindActors(newActors: List<CastItem?>?) {
        listActors = newActors
       notifyDataSetChanged()
    }

    inner class ActorListViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        var actorImage: ImageView? = null
        var actorName: TextView? = null
        init {
            actorImage = itemView.findViewById(R.id.actor_image)
            actorName = itemView.findViewById(R.id.actor_name)
        }
        fun onBind(actor:CastItem)
        {
                val urlStr = baseURlBackdrop + actor.profilePath
                actorName?.text = actor.name
                actorImage?.load(urlStr)
                Log.d("Actor",urlStr)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorListViewHolder {
        return ActorListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.view_holder_actor, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ActorListViewHolder, position: Int) {
        listActors?.get(position)?.let { holder.onBind(it) }
    }

    override fun getItemCount(): Int {
        return listActors!!.size
    }
}