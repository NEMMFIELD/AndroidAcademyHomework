package com.example.androidacademyhomework.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.androidacademyhomework.R
import com.example.androidacademyhomework.Utils.Companion.actorUrl
import com.example.androidacademyhomework.database.ActorsEntity
import com.example.androidacademyhomework.databinding.ViewHolderActorBinding
import com.example.androidacademyhomework.model.ActorsDiffUtil

class ActorListAdapter() :
    ListAdapter<ActorsEntity,ActorListAdapter.ActorListViewHolder>(ActorsDiffUtil()) {
    private var listActors: MutableList<ActorsEntity> = mutableListOf()
    inner class ActorListViewHolder(private val binding: ViewHolderActorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(actor: ActorsEntity) {
            binding.apply {
           actorImage.load(actorUrl + actor.profilePath)
            actorName.text = actor.name
            }
        }
    }

    override fun submitList(list: List<ActorsEntity>?) {
        super.submitList(list?.let { ArrayList(it) })
        listActors = list?.toMutableList()?:ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorListViewHolder {
        val binding = ViewHolderActorBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ActorListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActorListViewHolder, position: Int) {
        val actors: ActorsEntity = listActors[position]
        holder.bind(actors)
    }

    override fun getItemCount(): Int {
        return listActors.size
    }
}
private val RecyclerView.ViewHolder.context
    get() = this.itemView.context