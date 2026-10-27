package com.route.domain.data

data class SignUpRequest(
    val name: String,
    val email: String,
    val password: String,
    val rePassword: String,
    val phone: String,
)
