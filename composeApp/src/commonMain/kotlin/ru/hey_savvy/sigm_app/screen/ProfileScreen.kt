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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import ru.hey_savvy.sigm_app.view.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    val profile by viewModel.profile.collectAsState()
    var editingStatus by remember { mutableStateOf(false) }
    var editingFirstName by remember { mutableStateOf(false) }
    var editingLastName by remember { mutableStateOf(false) }
    var statusText by remember { mutableStateOf("") }
    var firstNameText by remember { mutableStateOf("") }
    var lastNameText by remember { mutableStateOf("") }

    LaunchedEffect(profile) {
        statusText = profile?.status ?: ""
        firstNameText = profile?.firstName ?: ""
        lastNameText = profile?.lastName ?: ""
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Профиль") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Text("←") }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color(0xFF7F77DD), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = profile?.username?.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = profile?.username ?: "", fontSize = 24.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(24.dp))

            ProfileField(
                label = "Имя",
                value = firstNameText,
                isEditing = editingFirstName,
                onValueChange = { firstNameText = it },
                onEdit = { editingFirstName = true },
                onSave = { viewModel.updateFirstName(firstNameText); editingFirstName = false }
            )

            ProfileField(
                label = "Фамилия",
                value = lastNameText,
                isEditing = editingLastName,
                onValueChange = { lastNameText = it },
                onEdit = { editingLastName = true },
                onSave = { viewModel.updateLastName(lastNameText); editingLastName = false }
            )

            ProfileField(
                label = "Статус",
                value = statusText,
                isEditing = editingStatus,
                onValueChange = { statusText = it },
                onEdit = { editingStatus = true },
                onSave = { viewModel.updateStatus(statusText); editingStatus = false }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Выйти из аккаунта", color = Color.White)
            }
        }
    }
}

@Composable
fun ProfileField(
    label: String,
    value: String,
    isEditing: Boolean,
    onValueChange: (String) -> Unit,
    onEdit: () -> Unit,
    onSave: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        if (isEditing) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                TextButton(onClick = onSave) { Text("Сохранить") }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = value.ifBlank { "Не указано" })
                TextButton(onClick = onEdit) { Text("Изменить", color = Color(0xFF7F77DD)) }
            }
        }
        HorizontalDivider()
    }
}