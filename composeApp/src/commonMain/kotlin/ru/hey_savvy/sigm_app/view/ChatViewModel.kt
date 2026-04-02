package ru.hey_savvy.sigm_app.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.hey_savvy.sigm_app.model.Message
import ru.hey_savvy.sigm_app.repository.ChatRepository


class ChatViewModel(
    private val roomId: String,
    private val repository: ChatRepository
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    init {
        loadMessages()
        observeIncoming()
    }

    private fun loadMessages() {
        viewModelScope.launch {
            _messages.value = repository.getMessages(roomId)
        }
    }

    private fun observeIncoming() {
        viewModelScope.launch {
            repository.connect(roomId).collect { message ->
                _messages.value += message
            }
        }
    }

     fun sendMessage(text: String, author: String) {
         viewModelScope.launch {
             repository.send(author, text)
         }

    }
}