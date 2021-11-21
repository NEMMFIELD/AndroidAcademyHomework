package com.example.androidacademyhomework.model

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.example.androidacademyhomework.database.MovieEntity

class MovieDiffUtil():DiffUtil.ItemCallback<MovieEntity>() {

    override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean =
        oldItem.id == newItem.id


    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean =
        oldItem == newItem

}