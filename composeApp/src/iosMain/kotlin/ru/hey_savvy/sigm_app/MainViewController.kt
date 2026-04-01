package ru.hey_savvy.sigm_app

import ChatScreen
import LoginScreen
import RoomsScreen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.ComposeUIViewController
import ru.hey_savvy.sigm_app.model.Room
import ru.hey_savvy.sigm_app.view.ChatViewModel
import ru.hey_savvy.sigm_app.view.RoomsViewModel

fun MainViewController() = ComposeUIViewController {
    var currentUser by remember { mutableStateOf<String?>(null) }

    var currentRoom by remember { mutableStateOf<Room?>(null) }
    val roomsViewModel = remember { RoomsViewModel() }

    if (currentUser == null) {
        LoginScreen(onLogin = { currentUser = it })
    } else if (currentRoom == null) {
            RoomsScreen(viewModel = roomsViewModel, onRoomClick = { currentRoom = it })
    } else {
        val chatViewModel = remember(currentRoom) { ChatViewModel(currentRoom!!.id) }
        ChatScreen(
            username = currentUser!!,
            viewModel = chatViewModel,
            room = currentRoom!!,
            onBack = { currentRoom = null }
        )
    }
}