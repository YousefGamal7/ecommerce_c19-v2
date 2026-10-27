package com.route.data.dataSource

import com.route.domain.data.User

interface AuthLocalDataSource {
    fun saveSession(user: User, token: String)
    fun getLoggedUser(): User?
    fun getToken(): String?
    fun clearSession()
}
