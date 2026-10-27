package com.route.api.data

import com.google.gson.annotations.SerializedName
import com.route.domain.data.User

data class UserDto(
	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("role")
	val role: String? = null,
) {
	fun toUser(): User? {
		if (name == null || email == null) return null
		return User(name = name, email = email, role = role)
	}
}
