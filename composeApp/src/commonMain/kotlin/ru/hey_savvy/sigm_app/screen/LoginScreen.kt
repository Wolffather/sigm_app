package ru.hey_savvy.sigm_app.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import ru.hey_savvy.sigm_app.repository.AuthRepository
import ru.hey_savvy.sigm_app.view.LoginViewModel

@Composable
fun LoginScreen(viewModel: LoginViewModel, onLogin: (String) -> Unit) {

    val token by viewModel.token.collectAsState()
    val error by viewModel.error.collectAsState()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isRegistering by remember { mutableStateOf(false) }

    LaunchedEffect(token) {
        if (token != null) onLogin(username)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "sigm",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF7F77DD)
        )

        Spacer(modifier = Modifier.height(48.dp))

        TextField(
            value = username,
            onValueChange = { username = it },
            placeholder = { Text("имя пользователя") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("пароль") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        error?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (isRegistering) viewModel.register(username, password)
                else viewModel.login(username, password)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isRegistering) "зарегистрироваться" else "войти")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = { isRegistering = !isRegistering }) {
            Text(
                text = if (isRegistering) "уже есть аккаунт? войти"
                else "нет аккаунта? зарегистрироваться",
                color = Color(0xFF7F77DD)
            )
        }
    }
}