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
import ru.hey_savvy.sigm_app.model.ChangePasswordRequest
import ru.hey_savvy.sigm_app.model.ChangeUsernameRequest
import ru.hey_savvy.sigm_app.model.ProfileUpdate
import ru.hey_savvy.sigm_app.model.UserProfile
import ru.hey_savvy.sigm_app.model.UserStatus

class UserRepository {
    private val client = ApiClient.client

    private fun authHeader() = "Bearer ${ApiClient.token}"

    suspend fun getProfile(): UserProfile {
        return client.get("${Config.HTTP_URL}/users/me") {
            header(HttpHeaders.Authorization, authHeader())
        }.body()
    }

    suspend fun updateProfile(
        status: UserStatus? = null,
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

    suspend fun changePassword(currentPassword: String, newPassword: String): Boolean {
        val response = client.put("${Config.HTTP_URL}/users/me/password") {
            header(HttpHeaders.Authorization, authHeader())
            contentType(ContentType.Application.Json)
            setBody(ChangePasswordRequest(currentPassword, newPassword))
        }
        return response.status.value == 200
    }

    suspend fun changeUsername(newUsername: String, currentPassword: String): Boolean {
        val response = client.put("${Config.HTTP_URL}/users/me/username") {
            header(HttpHeaders.Authorization, authHeader())
            contentType(ContentType.Application.Json)
            setBody(ChangeUsernameRequest(newUsername, currentPassword))
        }
        return response.status.value == 200
    }
}