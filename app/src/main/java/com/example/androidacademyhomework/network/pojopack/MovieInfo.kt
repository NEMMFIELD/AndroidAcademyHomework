package com.example.androidacademyhomework.network.pojopack

import com.google.gson.annotations.SerializedName

data class MovieInfo(

	@SerializedName("original_language")
	val originalLanguage: String? = null,

	@SerializedName("imdb_id")
	val imdbId: String? = null,

	@SerializedName("video")
	val video: Boolean? = null,

	@SerializedName("title")
	val title: String? = null,

	@SerializedName("backdrop_path")
	val backdropPath: String? = null,

	@SerializedName("revenue")
	val revenue: Int? = null,

	@SerializedName("genres")
	val genres: List<GenresItem?>? = null,

	@SerializedName("popularity")
	val popularity: Double? = null,

	@SerializedName("production_countries")
	val productionCountries: List<Any?>? = null,

	@SerializedName("id")
	val id: Long? = null,

	@SerializedName("vote_count")
	val voteCount: Int? = null,

	@SerializedName("budget")
	val budget: Int? = null,

	@SerializedName("overview")
	val overview: String? = null,

	@SerializedName("original_title")
	val originalTitle: String? = null,

	@SerializedName("runtime")
	val runtime: Long? = null,

	@SerializedName("poster_path")
	val posterPath: String? = null,

	@SerializedName("spoken_languages")
	val spokenLanguages: List<SpokenLanguagesItem?>? = null,

	@SerializedName("production_companies")
	val productionCompanies: List<ProductionCompaniesItem?>? = null,

	@SerializedName("release_date")
	val releaseDate: String? = null,

	@SerializedName("vote_average")
	val voteAverage: Double? = null,

	@SerializedName("belongs_to_collection")
	val belongsToCollection: Any? = null,

	@SerializedName("tagline")
	val tagline: String? = null,

	@SerializedName("adult")
	val adult: Boolean? = null,

	@SerializedName("homepage")
	val homepage: String? = null,

	@SerializedName("status")
	val status: String? = null
)

data class GenresItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class ProductionCompaniesItem(

	@field:SerializedName("logo_path")
	val logoPath: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("origin_country")
	val originCountry: String? = null
)

data class SpokenLanguagesItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("iso_639_1")
	val iso6391: String? = null,

	@field:SerializedName("english_name")
	val englishName: String? = null
)
