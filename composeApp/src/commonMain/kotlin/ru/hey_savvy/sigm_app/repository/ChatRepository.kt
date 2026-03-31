package ru.hey_savvy.sigm_app.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import ru.hey_savvy.sigm_app.model.Message


class ChatRepository {

    private var session: WebSocketSession? = null

    private val client = HttpClient {
        install(WebSockets)
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun getMessages(): List<Message>{
        return client.get("http://192.168.0.103:8080/messages").body()
    }

    suspend fun send(author: String, text: String) {
        val json = """{"author":"$author","text":"$text"}"""
        session?.send(Frame.Text(json))
    }

    fun connect(): Flow<Message> = flow {
        client.webSocket("ws://192.168.0.103:8080/chat") {
            session = this
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val message = Json.decodeFromString<Message>(frame.readText())
                    emit(message)
                }
            }
        }
    }

}

