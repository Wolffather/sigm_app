package ru.hey_savvy.sigm_app.repository

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import ru.hey_savvy.sigm_app.Config
import ru.hey_savvy.sigm_app.model.Room
import ru.hey_savvy.sigm_app.model.RoomType

class RoomRepository {
    private val client = ApiClient.client

    private fun authHeader() = "Bearer ${ApiClient.token}"

    suspend fun getRooms(): List<Room> {
        return client.get("${Config.HTTP_URL}/rooms") {
            header(HttpHeaders.Authorization, authHeader())
        }.body()
    }

    suspend fun getPublicRooms(): List<Room> {
        return client.get("${Config.HTTP_URL}/rooms/public") {
            header(HttpHeaders.Authorization, authHeader())
        }.body()
    }

    suspend fun createRoom(name: String, type: RoomType): Room {
        return client.post("${Config.HTTP_URL}/rooms") {
            header(HttpHeaders.Authorization, authHeader())
            contentType(ContentType.Application.Json)
            setBody(Room(id = 0L, name = name, type = type))
        }.body()
    }

    suspend fun joinRoom(roomId: Long) {
        client.post("${Config.HTTP_URL}/rooms/$roomId/join") {
            header(HttpHeaders.Authorization, authHeader())
        }
    }

    suspend fun startPrivateChat(username: String): Room {
        return client.post("${Config.HTTP_URL}/chats/private/$username") {
            header(HttpHeaders.Authorization, authHeader())
        }.body()
    }
}