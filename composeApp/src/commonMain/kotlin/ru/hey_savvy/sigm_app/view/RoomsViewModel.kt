package ru.hey_savvy.sigm_app.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.hey_savvy.sigm_app.model.Room
import ru.hey_savvy.sigm_app.model.RoomType
import ru.hey_savvy.sigm_app.repository.RoomRepository

class RoomsViewModel(private val repository: RoomRepository) : ViewModel() {

    private val _publicRooms = MutableStateFlow<List<Room>>(emptyList())
    private val _rooms = MutableStateFlow<List<Room>>(emptyList())
    val rooms: StateFlow<List<Room>> = _rooms
    val publicRooms: StateFlow<List<Room>> = _publicRooms

    init {
        loadRooms()
    }

    fun loadRooms() {
        viewModelScope.launch {
            _rooms.value = repository.getRooms()
        }
    }

    fun loadPublicRooms() {
        viewModelScope.launch {
            _publicRooms.value = repository.getPublicRooms()
        }
    }

    fun createRoom(name: String, type: RoomType) {
        viewModelScope.launch {
            val newRoom = repository.createRoom(name, type)
            _rooms.value += newRoom
        }
    }

    fun joinRoom(roomId: Long) {
        viewModelScope.launch {
            repository.joinRoom(roomId)
            loadRooms()
        }
    }

    fun startPrivateChat(username: String, onSuccess: (Room) -> Unit) {
        viewModelScope.launch {
            val room = repository.startPrivateChat(username)
            loadRooms()
            onSuccess(room)
        }
    }
}