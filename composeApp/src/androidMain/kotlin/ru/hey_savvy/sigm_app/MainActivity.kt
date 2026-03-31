package ru.hey_savvy.sigm_app

import ChatScreen
import LoginScreen
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            var currentUser by remember { mutableStateOf<String?>(null) }

            if (currentUser == null) {
                LoginScreen(onLogin = { currentUser = it })
            } else {
                ChatScreen(username = currentUser!!)
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}