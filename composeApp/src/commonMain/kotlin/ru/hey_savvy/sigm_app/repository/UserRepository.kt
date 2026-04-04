package ru.hey_savvy.sigm_app.repository

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import ru.hey_savvy.sigm_app.Config
import ru.hey_savvy.sigm_app.model.ProfileUpdate
import ru.hey_savvy.sigm_app.model.UserProfile

class UserRepository {
    private val client = ApiClient.client

    private fun authHeader() = "Bearer ${ApiClient.token}"

    suspend fun getProfile(): UserProfile {
        return client.get("${Config.HTTP_URL}/users/me") {
            header(HttpHeaders.Authorization, authHeader())
        }.body()
    }

    suspend fun updateProfile(status: String? = null, avatarUrl: String? = null) {
        client.put("${Config.HTTP_URL}/users/me") {
            header(HttpHeaders.Authorization, authHeader())
            contentType(ContentType.Application.Json)
            setBody(ProfileUpdate(status = status, avatarUrl = avatarUrl))
        }
    }

    suspend fun updateProfile(
        status: String? = null,
        avatarUrl: String? = null,
        firstName: String? = null,
        lastName: String? = null
    ) {
        client.put("${Config.HTTP_URL}/users/me") {
            header(HttpHeaders.Authorization, authHeader())
            contentType(ContentType.Application.Json)
            setBody(ProfileUpdate(
                status = status,
                avatarUrl = avatarUrl,
                firstName = firstName,
                lastName = lastName
            ))
        }
    }
}