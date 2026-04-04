package ru.hey_savvy.sigm_app

import androidx.compose.runtime.LaunchedEffect
import ru.hey_savvy.sigm_app.screen.ChatScreen
import ru.hey_savvy.sigm_app.screen.LoginScreen
import ru.hey_savvy.sigm_app.screen.RoomsScreen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController
import ru.hey_savvy.sigm_app.model.Room
import ru.hey_savvy.sigm_app.repository.ApiClient
import ru.hey_savvy.sigm_app.repository.AuthRepository
import ru.hey_savvy.sigm_app.repository.MessageRepository
import ru.hey_savvy.sigm_app.repository.RoomRepository
import ru.hey_savvy.sigm_app.repository.UserRepository
import ru.hey_savvy.sigm_app.screen.ProfileScreen
import ru.hey_savvy.sigm_app.view.ChatViewModel
import ru.hey_savvy.sigm_app.view.LoginViewModel
import ru.hey_savvy.sigm_app.view.ProfileViewModel
import ru.hey_savvy.sigm_app.view.RoomsViewModel

fun MainViewController(): UIViewController {
    TokenStorage.getToken()?.let {
        ApiClient.token = it
    }

    return ComposeUIViewController {
        val authRepository = remember { AuthRepository() }
        val roomRepository = remember { RoomRepository() }
        val messageRepository = remember { MessageRepository() }
        val userRepository = remember { UserRepository() }

        var showProfile by remember { mutableStateOf(false) }

        val loginViewModel = remember { LoginViewModel(authRepository) }

        var currentUser by remember {
            mutableStateOf(TokenStorage.getUsername())
        }
        var currentRoom by remember { mutableStateOf<Room?>(null) }

        if (currentUser == null) {
            LoginScreen(
                viewModel = loginViewModel,
                onLogin = { username -> currentUser = username }
            )
        } else if (showProfile) {
            val profileViewModel = remember { ProfileViewModel(userRepository) }
            ProfileScreen(
                viewModel = profileViewModel,
                onBack = { showProfile = false },
                onLogout = {
                    loginViewModel.logout()
                    currentUser = null
                    currentRoom = null
                }
            )
        } else if (currentRoom == null) {
            val roomsViewModel = remember { RoomsViewModel(roomRepository) }
            RoomsScreen(
                viewModel = roomsViewModel,
                onRoomClick = { currentRoom = it },
                onProfileClick = { showProfile = true },
                currentUsername = currentUser!!
            )
        } else {
            val chatViewModel = remember(currentRoom) { ChatViewModel(currentRoom!!.id.toString(), messageRepository) }
            ChatScreen(
                username = currentUser!!,
                viewModel = chatViewModel,
                room = currentRoom!!,
                onBack = { currentRoom = null }
            )
        }
    }
}