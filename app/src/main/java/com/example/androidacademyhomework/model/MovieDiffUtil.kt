package com.example.androidacademyhomework.model

import androidx.recyclerview.widget.DiffUtil

class MovieDiffUtil(private val oldList:List<Model>,private val newList:List<Model>):DiffUtil.Callback() {
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