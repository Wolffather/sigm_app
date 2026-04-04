package ru.hey_savvy.sigm_app

import com.russhwolf.settings.Settings

object TokenStorage {
    private val settings by lazy { Settings() }
    private const val KEY_TOKEN = "auth_token"
    private const val KEY_USERNAME = "username"

    fun saveToken(token: String) {
        settings.putString(KEY_TOKEN, token)
    }

    fun getToken(): String? {
        return settings.getStringOrNull(KEY_TOKEN)
    }

    fun saveUsername(username: String) {
        settings.putString(KEY_USERNAME, username)
    }

    fun getUsername(): String? {
        return settings.getStringOrNull(KEY_USERNAME)
    }

    fun clear() {
        settings.remove(KEY_TOKEN)
        settings.remove(KEY_USERNAME)
    }
}