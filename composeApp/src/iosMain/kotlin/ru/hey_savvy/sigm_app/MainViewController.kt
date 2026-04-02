package ru.hey_savvy.sigm_app

import ChatScreen
import LoginScreen
import RoomsScreen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.ComposeUIViewController
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.hey_savvy.sigm_app.model.Room
import ru.hey_savvy.sigm_app.repository.ChatRepository
import ru.hey_savvy.sigm_app.view.ChatViewModel
import ru.hey_savvy.sigm_app.view.LoginViewModel
import ru.hey_savvy.sigm_app.view.RoomsViewModel

fun MainViewController() = ComposeUIViewController {
    var currentUser by remember { mutableStateOf<String?>(null) }
    var currentRoom by remember { mutableStateOf<Room?>(null) }
    var repository by remember { mutableStateOf<ChatRepository?>(null) }

    val loginViewModel = viewModel<LoginViewModel>()

    if (currentUser == null) {
        LoginScreen(
            viewModel = loginViewModel,
            onLogin = { username, repo ->
                currentUser = username
                repository = repo
            }
        )
    } else if (currentRoom == null) {
        val roomsViewModel = remember(repository) { RoomsViewModel(repository!!) }
        RoomsScreen(viewModel = roomsViewModel, onRoomClick = { currentRoom = it })
    } else {
        val chatViewModel = remember(currentRoom) { ChatViewModel(currentRoom!!.id.toString(), repository!!) }
        ChatScreen(username = currentUser!!, viewModel = chatViewModel, room = currentRoom!!, onBack = { currentRoom = null })
    }
}
