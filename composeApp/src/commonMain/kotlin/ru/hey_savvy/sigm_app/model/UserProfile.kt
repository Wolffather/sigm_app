package ru.hey_savvy.sigm_app.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val username: String,
    val status: String,
    val avatarUrl: String
)