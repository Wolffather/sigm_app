package ru.hey_savvy.sigm_app.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.hey_savvy.sigm_app.model.Room
import ru.hey_savvy.sigm_app.model.RoomType
import ru.hey_savvy.sigm_app.repository.ChatRepository

class RoomsViewModel(private val repository: ChatRepository) : ViewModel() {

    private val _rooms = MutableStateFlow<List<Room>>(emptyList())
    val rooms: StateFlow<List<Room>> = _rooms

    init {
        loadRooms()
    }

    fun loadRooms() {
        viewModelScope.launch {
            _rooms.value = repository.getRooms()
        }
    }

    fun createRoom(name: String, type: RoomType) {
        viewModelScope.launch {
            val newRoom = repository.createRoom(name, type)
            _rooms.value += newRoom
        }
    }
}