package ru.hey_savvy.sigm_app.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.hey_savvy.sigm_app.extension.clearFocusOnTap
import ru.hey_savvy.sigm_app.model.UserStatus
import ru.hey_savvy.sigm_app.view.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    val profile by viewModel.profile.collectAsState()
    var editingFirstName by remember { mutableStateOf(false) }
    var editingLastName by remember { mutableStateOf(false) }
    var firstNameText by remember { mutableStateOf("") }
    var lastNameText by remember { mutableStateOf("") }
    var showStatusMenu by remember { mutableStateOf(false) }

    var showUsernameDialog by remember { mutableStateOf(false) }
    var newUsername by remember { mutableStateOf("") }
    var usernamePassword by remember { mutableStateOf("") }
    var usernameError by remember { mutableStateOf<String?>(null) }

    var showPasswordDialog by remember { mutableStateOf(false) }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(profile) {
        firstNameText = profile?.firstName ?: ""
        lastNameText = profile?.lastName ?: ""
    }

    if (showUsernameDialog) {
        AlertDialog(
            onDismissRequest = { showUsernameDialog = false },
            title = { Text("Сменить имя пользователя") },
            text = {
                Column {
                    TextField(
                        value = newUsername,
                        onValueChange = { newUsername = it },
                        placeholder = { Text("Новый юзернейм") },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = usernamePassword,
                        onValueChange = { usernamePassword = it },
                        placeholder = { Text("Текущий пароль") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation()
                    )
                    usernameError?.let {
                        Text(it, color = Color.Red, fontSize = 12.sp)
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.changeUsername(newUsername, usernamePassword) { success ->
                        if (success) {
                            showUsernameDialog = false
                            newUsername = ""
                            usernamePassword = ""
                            usernameError = null
                        } else {
                            usernameError = "Неверный пароль или имя занято"
                        }
                    }
                }) { Text("Сохранить") }
            },
            dismissButton = {
                Button(onClick = { showUsernameDialog = false }) { Text("Отмена") }
            }
        )
    }

    if (showPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showPasswordDialog = false },
            title = { Text("Сменить пароль") },
            text = {
                Column {
                    TextField(
                        value = currentPassword,
                        onValueChange = { currentPassword = it },
                        placeholder = { Text("Текущий пароль") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        placeholder = { Text("Новый пароль") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation()
                    )
                    passwordError?.let {
                        Text(it, color = Color.Red, fontSize = 12.sp)
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.changePassword(currentPassword, newPassword) { success ->
                        if (success) {
                            showPasswordDialog = false
                            currentPassword = ""
                            newPassword = ""
                            passwordError = null
                        } else {
                            passwordError = "Неверный текущий пароль"
                        }
                    }
                }) { Text("Сохранить") }
            },
            dismissButton = {
                Button(onClick = { showPasswordDialog = false }) { Text("Отмена") }
            }
        )
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .clearFocusOnTap(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(MaterialTheme.colorScheme.primary, shape = CircleShape),
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

            Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Text("Статус", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                Box {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showStatusMenu = true }
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(statusLabel(profile?.status ?: UserStatus.AVAILABLE))
                        Text("▾", color = MaterialTheme.colorScheme.primary)
                    }
                    DropdownMenu(
                        expanded = showStatusMenu,
                        onDismissRequest = { showStatusMenu = false }
                    ) {
                        UserStatus.entries.forEach { status ->
                            DropdownMenuItem(
                                text = { Text(statusLabel(status)) },
                                onClick = {
                                    viewModel.updateStatus(status)
                                    showStatusMenu = false
                                }
                            )
                        }
                    }
                }
                HorizontalDivider()
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Имя пользователя")
                TextButton(onClick = { showUsernameDialog = true }) {
                    Text("Изменить", color = MaterialTheme.colorScheme.primary)
                }
            }
            HorizontalDivider()

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Пароль")
                TextButton(onClick = { showPasswordDialog = true }) {
                    Text("Изменить", color = MaterialTheme.colorScheme.primary)
                }
            }
            HorizontalDivider()

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
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
                TextButton(onClick = onEdit) { Text("Изменить", color = MaterialTheme.colorScheme.primary) }
            }
        }
        HorizontalDivider()
    }
}

fun statusLabel(status: UserStatus): String = when (status) {
    UserStatus.AVAILABLE -> "🟢 Доступен"
    UserStatus.BUSY -> "🔴 Занят"
    UserStatus.DO_NOT_DISTURB -> "⛔ Не беспокоить"
    UserStatus.AWAY -> "🟡 Отошёл"
    UserStatus.INVISIBLE -> "⚫ Невидимый"
}