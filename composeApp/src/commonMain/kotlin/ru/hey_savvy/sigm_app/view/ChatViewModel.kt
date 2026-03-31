package ru.hey_savvy.sigm_app.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.hey_savvy.sigm_app.model.Message
import ru.hey_savvy.sigm_app.repository.ChatRepository
import kotlin.time.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.Instant


class ChatViewModel : ViewModel() {

    private val repository = ChatRepository()

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    init {
        loadMessages()
        observeIncoming()
    }

    private fun loadMessages() {
        viewModelScope.launch {
            _messages.value = repository.getMessages()
        }
    }

    private fun observeIncoming() {
        viewModelScope.launch {
            repository.connect().collect { text ->
                val now = Instant.fromEpochMilliseconds(Clock.System.now().toEpochMilliseconds())
                val newMessage = Message(
                    id = now.toEpochMilliseconds(),
                    text = text,
                    author = "unknown",
                    timestamp = now.toLocalDateTime(TimeZone.currentSystemDefault())
                )
                _messages.value += newMessage
            }
        }
    }

     fun sendMessage(text: String) {
         viewModelScope.launch {
             repository.send(text)
         }

    }
}