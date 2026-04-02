package ru.hey_savvy.sigm_app.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.hey_savvy.sigm_app.repository.ChatRepository

class LoginViewModel : ViewModel() {
    private val repository = ChatRepository()

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                val token = repository.login(username, password)
                _token.value = token
            } catch (e: Exception) {
                _error.value = "Неверный логин или пароль"
            }
        }
    }

    fun register(username: String, password: String) {
        viewModelScope.launch {
            try {
                repository.register(username, password)
                login(username, password) // сразу логинимся после регистрации
            } catch (e: Exception) {
                _error.value = "Ошибка регистрации"
            }
        }
    }

    fun getRepository() = repository
}