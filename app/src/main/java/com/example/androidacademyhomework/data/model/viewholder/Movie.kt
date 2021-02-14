package com.example.androidacademyhomework.data.model.viewholder

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Movie(
	@SerializedName("dates")
	val dates: Dates? = null,

	@SerializedName("page")
	val page: Int? = null,

	@SerializedName("total_pages")
	val totalPages: Int? = null,

	@SerializedName("results")
	val results: List<ResultsItem?>? = null,

	@SerializedName("total_results")
	val totalResults: Int? = null
)

data class ResultsItem(
	@SerializedName("overview")
	val overview: String? = null,

	@SerializedName("original_language")
	val originalLanguage: String? = null,

	@SerializedName("original_title")
	val originalTitle: String? = null,

	@SerializedName("video")
	val video: Boolean? = null,


	@SerializedName("title")
	val title: String? = null,

	@SerializedName("genre_ids")
	val genreIds: List<Int?>? = null,

	@SerializedName("poster_path")
	val posterPath: String? = null,

	@SerializedName("backdrop_path")
	val backdropPath: String? = null,

	@SerializedName("release_date")
	val releaseDate: String? = null,

	@SerializedName("popularity")
	val popularity: Double? = null,

	@SerializedName("vote_average")
	val voteAverage: Float? = null,

	@SerializedName("id")
	val id: Long? = null,

	@SerializedName("adult")
	val adult: Boolean? = null,

	@SerializedName("vote_count")
	val voteCount: Int? = null
)

data class Dates(
	@SerializedName("maximum")
	val maximum: String? = null,

	@SerializedName("minimum")
	val minimum: String? = null
)
