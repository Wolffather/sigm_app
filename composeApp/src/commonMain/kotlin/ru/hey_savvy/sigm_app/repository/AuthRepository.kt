package ru.hey_savvy.sigm_app.repository

import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.hey_savvy.sigm_app.Config
import ru.hey_savvy.sigm_app.model.AuthResponse
import ru.hey_savvy.sigm_app.model.User

class AuthRepository {
    private val client = ApiClient.client

    suspend fun login(username: String, password: String): String {
        val response = client.post("${Config.HTTP_URL}/login") {
            contentType(ContentType.Application.Json)
            setBody(User(username, password))
        }.body<AuthResponse>()
        ApiClient.token = response.token
        return response.token
    }

    suspend fun register(username: String, password: String) {
        client.post("${Config.HTTP_URL}/register") {
            contentType(ContentType.Application.Json)
            setBody(User(username, password))
        }
    }

    fun logout() {
        ApiClient.token = null
    }
}