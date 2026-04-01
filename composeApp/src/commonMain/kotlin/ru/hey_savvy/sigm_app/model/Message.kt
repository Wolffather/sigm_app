package ru.hey_savvy.sigm_app.model

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: String = "",
    val text: String,
    val author: String,
    val timestamp: String? = null
)
