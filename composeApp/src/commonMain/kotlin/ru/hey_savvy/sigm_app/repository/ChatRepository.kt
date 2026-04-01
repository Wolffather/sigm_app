package ru.hey_savvy.sigm_app.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import ru.hey_savvy.sigm_app.model.Message
import ru.hey_savvy.sigm_app.model.Room
import ru.hey_savvy.sigm_app.model.RoomType


class ChatRepository {

    private var session: WebSocketSession? = null

    private val client = HttpClient {
        install(WebSockets)
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun getMessages(roomId: Long): List<Message>{
        return client.get("http://192.168.0.103:8080/messages/$roomId").body()
    }

    suspend fun send(author: String, text: String) {
        val json = """{"author":"$author","text":"$text"}"""
        session?.send(Frame.Text(json))
    }

    suspend fun getRooms(): List<Room> {
        return client.get("http://192.168.0.103:8080/rooms").body()
    }

    suspend fun createRoom(name: String, type: RoomType): Room {
        return client.post("http://192.168.0.103:8080/rooms") {
            contentType(ContentType.Application.Json)
            setBody(Room(id = 0, name = name, type = type))
        }.body()
    }

    fun connect(roomId: Long): Flow<Message> = flow {
        client.webSocket("ws://192.168.0.103:8080/chat/$roomId") {
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

