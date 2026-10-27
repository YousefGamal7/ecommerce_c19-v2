package com.route.api.data

import com.google.gson.annotations.SerializedName
import com.route.domain.data.AuthResult

data class AuthResponseDto(
	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("user")
	val user: UserDto? = null,

	@field:SerializedName("token")
	val token: String? = null,

	@field:SerializedName("statusMsg")
	val statusMsg: String? = null,
) {
	fun toAuthResult(): AuthResult? {
		val domainUser = user?.toUser() ?: return null
		val validToken = token ?: return null
		return AuthResult(user = domainUser, token = validToken)
	}
}
