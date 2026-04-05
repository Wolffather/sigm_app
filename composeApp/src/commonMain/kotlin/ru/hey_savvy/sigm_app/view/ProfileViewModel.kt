package ru.hey_savvy.sigm_app.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.hey_savvy.sigm_app.model.UserProfile
import ru.hey_savvy.sigm_app.model.UserStatus
import ru.hey_savvy.sigm_app.repository.UserRepository

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {
    private val _profile = MutableStateFlow<UserProfile?>(null)
    val profile: StateFlow<UserProfile?> = _profile

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _profile.value = repository.getProfile()
        }
    }

    fun updateStatus(status: UserStatus) {
        viewModelScope.launch {
            repository.updateProfile(status = status)
            loadProfile()
        }
    }

    fun updateFirstName(firstName: String) {
        viewModelScope.launch {
            repository.updateProfile(firstName = firstName)
            loadProfile()
        }
    }

    fun updateLastName(lastName: String) {
        viewModelScope.launch {
            repository.updateProfile(lastName = lastName)
            loadProfile()
        }
    }

    fun changePassword(currentPassword: String, newPassword: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.changePassword(currentPassword, newPassword)
            onResult(success)
        }
    }

    fun changeUsername(newUsername: String, currentPassword: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.changeUsername(newUsername, currentPassword)
            if (success) loadProfile()
            onResult(success)
        }
    }
}