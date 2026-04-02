package ru.hey_savvy.sigm_app.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val username: String,
    val password: String
) {
}