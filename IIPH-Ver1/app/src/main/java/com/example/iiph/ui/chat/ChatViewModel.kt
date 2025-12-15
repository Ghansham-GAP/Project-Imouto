package com.example.iiph.ui.chat

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.iiph.data.SessionManager
import com.example.iiph.model.Message
import com.example.iiph.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ChatUiState {
    object Loading : ChatUiState()
    data class Success(val messages: List<Message>) : ChatUiState()
    data class Error(val message: String) : ChatUiState()
}

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application)

    private val _uiState = MutableStateFlow<ChatUiState>(ChatUiState.Loading)
    val uiState: StateFlow<ChatUiState> = _uiState

    fun getMessages() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getMessages()
                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = ChatUiState.Success(response.body()!!)
                } else {
                    _uiState.value = ChatUiState.Error("Failed to load messages")
                }
            } catch (e: Exception) {
                _uiState.value = ChatUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun sendMessage(messageText: String) {
        viewModelScope.launch {
            val senderId = sessionManager.getUserId()
            if (senderId == -1) {
                // Handle error: user not logged in
                return@launch
            }
            try {
                // Hardcoded receiver_id for simplicity
                RetrofitInstance.api.sendMessage(senderId, 1, messageText)
                // Refresh messages after sending
                getMessages()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}