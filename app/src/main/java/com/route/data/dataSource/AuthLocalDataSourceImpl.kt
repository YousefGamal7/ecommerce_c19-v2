package com.route.data.dataSource

import android.content.Context
import android.content.SharedPreferences
import com.route.domain.data.User
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AuthLocalDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : AuthLocalDataSource {

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences("route_ecommerce_auth", Context.MODE_PRIVATE)
    }

    override fun saveSession(user: User, token: String) {
        prefs.edit()
            .putString(KEY_TOKEN, token)
            .putString(KEY_NAME, user.name)
            .putString(KEY_EMAIL, user.email)
            .putString(KEY_ROLE, user.role)
            .apply()
    }

    override fun getLoggedUser(): User? {
        val token = prefs.getString(KEY_TOKEN, null)
        val name = prefs.getString(KEY_NAME, null)
        val email = prefs.getString(KEY_EMAIL, null)
        val role = prefs.getString(KEY_ROLE, null)

        if (token.isNullOrBlank() || email.isNullOrBlank() || name.isNullOrBlank()) {
            return null
        }
        return User(name = name, email = email, role = role)
    }

    override fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    override fun clearSession() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_NAME = "user_name"
        private const val KEY_EMAIL = "user_email"
        private const val KEY_ROLE = "user_role"
    }
}
