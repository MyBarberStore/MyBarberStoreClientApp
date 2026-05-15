package com.example.mybarberstore.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("barber_session", Context.MODE_PRIVATE)
    companion object {
        const val USER_TOKEN = "user_token"
        const val USER_NAME = "user_name"


    }

    fun saveAuthToken(token: String) {
        prefs.edit().putString(USER_TOKEN, token).apply()
    }

    fun saveClientId(id: Long) {
        val editor = prefs.edit()
        editor.putLong("CLIENT_ID", id)
        editor.apply()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun saveUserName(name: String) {
        prefs.edit().putString(USER_NAME, name).apply()
    }

    fun fetchClientId(): Long {
        // Si no lo encuentra, devuelve -1 como señal de error
        return prefs.getLong("CLIENT_ID", -1L)
    }

}