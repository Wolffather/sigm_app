package ru.hey_savvy.sigm_app.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
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
import androidx.compose.ui.unit.dp
import ru.hey_savvy.sigm_app.model.Room
import ru.hey_savvy.sigm_app.model.RoomType
import ru.hey_savvy.sigm_app.view.RoomsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomsScreen(
    viewModel: RoomsViewModel,
    onRoomClick: (Room) -> Unit,
    onLogout: () -> Unit,
    onProfileClick: () -> Unit
) {
    val rooms by viewModel.rooms.collectAsState()
    val publicRooms by viewModel.publicRooms.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var showPublicRooms by remember { mutableStateOf(false) }
    var roomName by remember { mutableStateOf("") }
    var roomType by remember { mutableStateOf(RoomType.CHAT) }
    var showPrivateChatDialog by remember { mutableStateOf(false) }
    var privateChatUsername by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadPublicRooms()
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Новая комната") },
            text = {
                Column {
                    TextField(
                        value = roomName,
                        onValueChange = { roomName = it },
                        placeholder = { Text("Название") }
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = roomType == RoomType.CHAT,
                            onClick = { roomType = RoomType.CHAT },
                            label = { Text("Чат") }
                        )
                        FilterChip(
                            selected = roomType == RoomType.CHANNEL,
                            onClick = { roomType = RoomType.CHANNEL },
                            label = { Text("Канал") }
                        )
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.createRoom(roomName, roomType)
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
                title = { Text("sigm") },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Text("👤")
                    }
                    IconButton(onClick = { showPublicRooms = !showPublicRooms }) {
                        Text("🔍")
                    }
                    IconButton(onClick = { showPrivateChatDialog = true }) {
                        Text("✉")
                    }
                    IconButton(onClick = {
                        onLogout()
                    }) {
                        Text("↩")
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
                Button(onClick = { showDialog = true }) {
                    Text("+")
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
                item { Text("Все комнаты", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.labelSmall) }
                items(publicRooms) { room ->
                    val isMember = rooms.any { it.id == room.id }
                    RoomItem(
                        room = room,
                        isMember = isMember,
                        onClick = { if (isMember) onRoomClick(room) },
                        onJoin = { viewModel.joinRoom(room.id) }
                    )
                }
            } else {
                item { Text("Мои комнаты", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.labelSmall) }
                items(rooms) { room ->
                    RoomItem(
                        room = room,
                        isMember = true,
                        onClick = { onRoomClick(room) },
                        onJoin = {}
                    )
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
            Text(text = room.type.toString(), style = MaterialTheme.typography.labelSmall)
        }
        if (!isMember) {
            Button(onClick = onJoin) {
                Text("вступить")
            }
        }
    }
}
