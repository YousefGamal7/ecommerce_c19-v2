package com.route.api.data

import com.google.gson.annotations.SerializedName

data class SignInRequestDto(
	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("password")
	val password: String? = null,
)
