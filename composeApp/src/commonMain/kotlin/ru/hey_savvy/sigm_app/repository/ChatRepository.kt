package ru.hey_savvy.sigm_app.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import ru.hey_savvy.sigm_app.Config
import ru.hey_savvy.sigm_app.model.AuthResponse
import ru.hey_savvy.sigm_app.model.Message
import ru.hey_savvy.sigm_app.model.Room
import ru.hey_savvy.sigm_app.model.RoomType
import ru.hey_savvy.sigm_app.model.User


class ChatRepository {

    private var session: WebSocketSession? = null
    private var token: String? = null

    private val client = HttpClient {
        install(WebSockets)
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun send(author: String, text: String) {
        val json = """{"author":"$author","text":"$text"}"""
        session?.send(Frame.Text(json))
    }

    suspend fun getRooms(): List<Room> {
        return client.get("${Config.HTTP_URL}/rooms") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }.body()
    }

    suspend fun getMessages(roomId: String): List<Message> {
        return client.get("${Config.HTTP_URL}/messages/$roomId") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }.body()
    }

    suspend fun createRoom(name: String, type: RoomType): Room {
        return client.post("${Config.HTTP_URL}/rooms") {
            header(HttpHeaders.Authorization, "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(Room(id = 0L, name = name, type = type))
        }.body()
    }

    suspend fun register(username: String, password: String) {
        val response = client.post("${Config.HTTP_URL}/register") {
            contentType(ContentType.Application.Json)
            setBody(User(username, password))
        }
    }

    suspend fun login(username: String, password: String): String {
        val response = client.post("${Config.HTTP_URL}/login") {
            contentType(ContentType.Application.Json)
            setBody(User(username, password))
        }.body<AuthResponse>()
        token = response.token
        return response.token
    }

    fun connect(roomId: String): Flow<Message> = flow {
        client.webSocket("${Config.WS_URL}/chat/$roomId?token=$token") {
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

