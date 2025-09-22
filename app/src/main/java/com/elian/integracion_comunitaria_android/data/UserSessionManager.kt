package com.elian.integracion_comunitaria_android.data

import android.content.Context
import android.content.SharedPreferences
import com.auth0.android.jwt.JWT

class UserSessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        private const val USER_TOKEN = "user_token"
    }

    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun getAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun isUserLoggedIn(): Boolean {
        return getAuthToken() != null
    }

    fun clearSession() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

    private fun decodeToken(): JWT? {
        val token = getAuthToken()
        return if (token != null) {
            JWT(token)
        } else {
            null
        }
    }

    fun getUserEmail(): String? {
        return decodeToken()?.getClaim("sub")?.asString()
    }

    fun getUserId(): Int? {
        return decodeToken()?.getClaim("userId")?.asInt()
    }
}