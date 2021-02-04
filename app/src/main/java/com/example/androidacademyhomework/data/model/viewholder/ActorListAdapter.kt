package com.example.androidacademyhomework.data.model.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.network.API_KEY
import com.example.androidacademyhomework.network.RetrofitModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActorListAdapter(private var listActors: List<CastItem?>?) :
    RecyclerView.Adapter<ActorListAdapter.ActorListViewHolder>() {
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorListViewHolder {
        return ActorListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.view_holder_actor, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ActorListViewHolder, position: Int) {
        val actors: List<CastItem?>? = listActors
        scope.launch {
            val config = RetrofitModule.moviesApi.getConfig(API_KEY)
            holder.actorName?.text = actors?.get(position)?.name
            holder.actorImage!!.load(
                config.images?.secureBaseUrl + config.images?.posterSizes?.get(
                    5
                ) + actors!![position]?.profilePath
            )
        }
    }

    override fun getItemCount(): Int {
        return listActors!!.size
    }
}