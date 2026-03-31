package ru.hey_savvy.sigm_app

import ChatScreen
import LoginScreen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() = ComposeUIViewController {
    var currentUser by remember { mutableStateOf<String?>(null) }

    if (currentUser == null) {
        LoginScreen(onLogin = { currentUser = it })
    } else {
        ChatScreen(username = currentUser!!)
    }
}