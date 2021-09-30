package com.example.androidacademyhomework.model

import androidx.recyclerview.widget.DiffUtil
import com.example.androidacademyhomework.database.MovieEntity

class MovieDiffUtil(private val oldList:List<MovieEntity>,private val newList:List<MovieEntity>):DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size


    override fun getNewListSize(): Int =  newList.size


    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem= oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem= oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem == newItem
    }
}