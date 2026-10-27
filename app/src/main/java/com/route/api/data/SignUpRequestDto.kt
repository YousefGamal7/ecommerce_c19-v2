package com.route.api.data

import com.google.gson.annotations.SerializedName

data class SignUpRequestDto(
	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("rePassword")
	val rePassword: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,
)
