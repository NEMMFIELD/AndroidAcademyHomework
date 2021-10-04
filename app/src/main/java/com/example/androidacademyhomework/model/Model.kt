package com.example.androidacademyhomework.model

import android.os.Parcelable
import com.example.androidacademyhomework.network.pojopack.ActorsResponse
import com.example.androidacademyhomework.network.pojopack.CastItem
import kotlinx.parcelize.Parcelize


@Parcelize
data class Model(
    val id: Long?,
    val pgAge: Boolean,
    val title: String?,
    val genres: List<String>?,
    val runningTime: Int,
    val reviewCount: Int,
    val isLiked: Boolean,
    val rating: Float,
    val imageUrl: String?,
    val detailImageUrl: String,
    val storyLine: String,
    val actors: List<CastItem>):Parcelable
