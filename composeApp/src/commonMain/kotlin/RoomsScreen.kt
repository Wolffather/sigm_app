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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.hey_savvy.sigm_app.model.Room
import ru.hey_savvy.sigm_app.model.RoomType
import ru.hey_savvy.sigm_app.view.RoomsViewModel

@Composable
fun RoomsScreen(viewModel: RoomsViewModel, onRoomClick: (Room) -> Unit) {

    val rooms by viewModel.rooms.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var roomName by remember { mutableStateOf("") }
    var roomType by remember { mutableStateOf(RoomType.CHAT) }

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
                }) {
                    Text("Создать")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }

    Scaffold(
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
            items(rooms) { room ->
                RoomItem(room) { onRoomClick(room) }
            }
        }
    }
}

@Composable
fun RoomItem(room: Room, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Text(text = room.name, style = MaterialTheme.typography.bodyLarge)
        Text(text = room.type.toString(), style = MaterialTheme.typography.labelSmall)
    }
}