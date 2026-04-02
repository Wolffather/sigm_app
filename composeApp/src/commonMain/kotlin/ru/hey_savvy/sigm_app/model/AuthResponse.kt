package ru.hey_savvy.sigm_app.model

import kotlinx.serialization.Serializable

@Serializable
class AuthResponse(
    val token: String
) {
}