package ru.hey_savvy.sigm_app.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.hey_savvy.sigm_app.view.ChatViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import ru.hey_savvy.sigm_app.model.Message
import ru.hey_savvy.sigm_app.model.Room
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Alignment

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ChatScreen(username: String, viewModel: ChatViewModel, room: Room, onBack: () -> Unit) {

    val messages by viewModel.messages.collectAsState()
    var inputText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(room.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("←")
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Сообщение...") }
                )
                Button(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            viewModel.sendMessage(inputText, username)
                            inputText = ""
                        }
                    }
                ) {
                    Text("→")
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            reverseLayout = true
        ) {
            items(messages.reversed()) { message ->
                MessageItem(message = message, currentUsername = username)
            }
        }
    }
}

@Composable
fun MessageItem(message: Message, currentUsername: String) {
    val isOwn = message.author == currentUsername

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (isOwn) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isOwn) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = message.author.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
        }

        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .background(
                    color = if (isOwn) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isOwn) 16.dp else 4.dp,
                        bottomEnd = if (isOwn) 4.dp else 16.dp
                    )
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Column {
                if (!isOwn) {
                    Text(
                        text = message.author,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                }
                Text(
                    text = message.text,
                    color = if (isOwn) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}