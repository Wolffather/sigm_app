package ru.hey_savvy.sigm_app.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.hey_savvy.sigm_app.model.Room
import ru.hey_savvy.sigm_app.model.RoomType
import ru.hey_savvy.sigm_app.view.RoomsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomsScreen(
    viewModel: RoomsViewModel,
    onRoomClick: (Room) -> Unit,
    onProfileClick: () -> Unit,
    currentUsername: String
) {
    val rooms by viewModel.rooms.collectAsState()
    val publicRooms by viewModel.publicRooms.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var showPublicRooms by remember { mutableStateOf(false) }
    var roomName by remember { mutableStateOf("") }
    var showPrivateChatDialog by remember { mutableStateOf(false) }
    var privateChatUsername by remember { mutableStateOf("") }
    var showMenu by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf(RoomType.CHAT) }

    LaunchedEffect(Unit) {
        viewModel.loadPublicRooms()
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(if (selectedType == RoomType.CHANNEL) "Новый канал" else "Новая группа") },
            text = {
                TextField(
                    value = roomName,
                    onValueChange = { roomName = it },
                    placeholder = { Text("Название") }
                )
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.createRoom(roomName, selectedType)
                    showDialog = false
                    roomName = ""
                }) { Text("Создать") }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) { Text("Отмена") }
            }
        )
    }

    if (showPrivateChatDialog) {
        AlertDialog(
            onDismissRequest = { showPrivateChatDialog = false },
            title = { Text("Новый чат") },
            text = {
                TextField(
                    value = privateChatUsername,
                    onValueChange = { privateChatUsername = it },
                    placeholder = { Text("имя пользователя") },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.startPrivateChat(privateChatUsername) { room ->
                        showPrivateChatDialog = false
                        privateChatUsername = ""
                        onRoomClick(room)
                    }
                }) { Text("Начать") }
            },
            dismissButton = {
                Button(onClick = { showPrivateChatDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(onClick = onProfileClick) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color(0xFF7F77DD), shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = currentUsername.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                },
            )
        },
        floatingActionButton = {
            Box {
                FloatingActionButton(onClick = { showMenu = true }) {
                    Text("+", fontSize = 24.sp)
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Новый чат") },
                        onClick = { showMenu = false; showPrivateChatDialog = true }
                    )
                    DropdownMenuItem(
                        text = { Text("Новая группа") },
                        onClick = { showMenu = false; showDialog = true; selectedType = RoomType.GROUP }
                    )
                    DropdownMenuItem(
                        text = { Text("Новый канал") },
                        onClick = { showMenu = false; showDialog = true; selectedType = RoomType.CHANNEL }
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (showPublicRooms) {
                item {
                    Text(
                        "Все комнаты",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                items(publicRooms) { room ->
                    val isMember = rooms.any { it.id == room.id }
                    RoomItem(
                        room = room,
                        isMember = isMember,
                        onClick = { if (isMember) onRoomClick(room) },
                        onJoin = { viewModel.joinRoom(room.id) }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                }
            } else {
                item {
                    Text(
                        "Мои комнаты",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                items(rooms) { room ->
                    RoomItem(
                        room = room,
                        isMember = true,
                        onClick = { onRoomClick(room) },
                        onJoin = {}
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    }
}

@Composable
fun RoomItem(room: Room, isMember: Boolean, onClick: () -> Unit, onJoin: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = room.name, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = when (room.type) {
                    RoomType.GROUP -> "группа"
                    RoomType.CHANNEL -> "канал"
                    RoomType.CHAT -> "чат"
                },
                style = MaterialTheme.typography.labelSmall
            )
        }
        if (!isMember) {
            Button(onClick = onJoin) {
                Text("вступить")
            }
        }
    }
}