package com.example.androidacademyhomework.network

import com.google.gson.annotations.SerializedName

data class Configuration(

	@field:SerializedName("images")
	val images: Images? = null,

	@field:SerializedName("change_keys")
	val changeKeys: List<String?>? = null
)

data class Images(

	@field:SerializedName("poster_sizes")
	val posterSizes: List<String?>? = null,

	@field:SerializedName("secure_base_url")
	val secureBaseUrl: String? = null,

	@field:SerializedName("backdrop_sizes")
	val backdropSizes: List<String?>? = null,

	@field:SerializedName("base_url")
	val baseUrl: String? = null,

	@field:SerializedName("logo_sizes")
	val logoSizes: List<String?>? = null,

	@field:SerializedName("still_sizes")
	val stillSizes: List<String?>? = null,

	@field:SerializedName("profile_sizes")
	val profileSizes: List<String?>? = null
)
