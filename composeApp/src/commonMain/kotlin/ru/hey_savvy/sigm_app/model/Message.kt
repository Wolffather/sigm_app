package ru.hey_savvy.sigm_app.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: Long,
    val text: String,
    val author: String,
    val timestamp: LocalDateTime
)
