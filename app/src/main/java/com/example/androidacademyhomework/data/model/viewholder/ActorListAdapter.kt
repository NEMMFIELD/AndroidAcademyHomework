package com.example.androidacademyhomework.data.model.viewholder

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.network.RetrofitModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActorListAdapter(private var listActors: Actor) :
    RecyclerView.Adapter<ActorListAdapter.ActorListViewHolder>() {
    val scope = CoroutineScope(Dispatchers.Main)

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
        val actors:Actor = listActors
        scope.launch {
        val config = RetrofitModule.moviesApi.getConfig()
        holder.actorName?.text = actors.cast?.get(position)?.name
        holder.actorImage!!.load( config.images?.secureBaseUrl + config.images?.posterSizes?.get(5)+actors.cast?.get(position)?.profilePath) }
    }

    override fun getItemCount(): Int {
        return listActors.cast!!.size
    }
}