package com.example.androidacademyhomework.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Model(
    val id: Int?,
    val pgAge: Int,
    val title: String?,
    val genres: List<Genre>?,
    val runningTime: Int,
    val reviewCount: Int,
    val isLiked: Boolean,
    val rating: Int,
    val imageUrl: String,
    val detailImageUrl: String,
    val storyLine: String,
    val actors: List<Actor>,):Parcelable
