package ru.hey_savvy.sigm_app

import ChatScreen
import LoginScreen
import RoomsScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.hey_savvy.sigm_app.model.Room
import ru.hey_savvy.sigm_app.view.ChatViewModel
import ru.hey_savvy.sigm_app.view.RoomsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            var currentUser by remember { mutableStateOf<String?>(null) }

            var currentRoom by remember { mutableStateOf<Room?>(null) }
            val roomsViewModel = viewModel<RoomsViewModel>()

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
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    LoginScreen(onLogin = {})
}