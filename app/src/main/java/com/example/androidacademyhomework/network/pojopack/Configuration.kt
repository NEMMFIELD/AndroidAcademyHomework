package com.example.androidacademyhomework.network.pojopack

import com.google.gson.annotations.SerializedName

data class Configuration(

	@SerializedName("images")
	val images: Images? = null,

	@SerializedName("change_keys")
	val changeKeys: List<String?>? = null
)

data class Images(

	@SerializedName("poster_sizes")
	val posterSizes: List<String?>? = null,

	@SerializedName("secure_base_url")
	val secureBaseUrl: String? = null,

	@SerializedName("backdrop_sizes")
	val backdropSizes: List<String?>? = null,

	@SerializedName("base_url")
	val baseUrl: String? = null,

	@SerializedName("logo_sizes")
	val logoSizes: List<String?>? = null,

	@SerializedName("still_sizes")
	val stillSizes: List<String?>? = null,

	@SerializedName("profile_sizes")
	val profileSizes: List<String?>? = null
)
