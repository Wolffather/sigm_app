package ru.hey_savvy.sigm_app.model

import kotlinx.serialization.Serializable

@Serializable
data class ProfileUpdate(
    val status: String? = null,
    val avatarUrl: String? = null
)