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
        return client.get("http://10.0.2.2:8080/messages").body()
    }

    suspend fun send(text: String) {
        session?.send(Frame.Text(text))
    }

    fun connect(): Flow<String> = flow {
        client.webSocket("ws://10.0.2.2:8080/chat") {
            session = this
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    emit(frame.readText())
                }
            }
        }
    }

}

