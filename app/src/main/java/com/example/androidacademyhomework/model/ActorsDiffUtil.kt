package com.example.androidacademyhomework.model

import androidx.recyclerview.widget.DiffUtil
import com.example.androidacademyhomework.database.ActorsEntity
import com.example.androidacademyhomework.database.MovieEntity

class ActorsDiffUtil() : DiffUtil.ItemCallback<ActorsEntity>() {
    override fun areItemsTheSame(oldItem: ActorsEntity, newItem: ActorsEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ActorsEntity, newItem: ActorsEntity): Boolean =
        oldItem == newItem
}