package ru.hey_savvy.sigm_app.repository

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.serialization.json.Json
import ru.hey_savvy.sigm_app.model.Message
import io.ktor.client.plugins.websocket.webSocket
import ru.hey_savvy.sigm_app.Config

class MessageRepository {
    private val client = ApiClient.client
    private var session: WebSocketSession? = null

    private fun authHeader() = "Bearer ${ApiClient.token}"

    suspend fun getMessages(roomId: String): List<Message> {
        return client.get("${Config.HTTP_URL}/messages/$roomId") {
            header(HttpHeaders.Authorization, authHeader())
        }.body()
    }

    suspend fun send(author: String, text: String) {
        val json = """{"author":"$author","text":"$text"}"""
        session?.send(Frame.Text(json))
    }

    fun connect(roomId: String): Flow<Message> = callbackFlow {
        client.webSocket("${Config.WS_URL}/chat/$roomId?token=${ApiClient.token}") {
            session = this
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val raw = frame.readText()
                    println("RAW FRAME: $raw")
                    val message = Json.decodeFromString<Message>(raw)
                    println("PARSED: $message")
                    trySend(message)
                }
            }
        }
        awaitClose()
    }
}