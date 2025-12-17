package com.example.iiph.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.iiph.data.SessionManager
import com.example.iiph.model.User
import com.example.iiph.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val user: User) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application)

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            try {
                val response = RetrofitInstance.api.login(email, pass)
                if (response.isSuccessful && response.body()?.status == "success") {
                    response.body()?.user?.let {
                        sessionManager.saveUserSession(it.currentYear, it.id)
                        _uiState.value = LoginUiState.Success(it)
                    } ?: run {
                        _uiState.value = LoginUiState.Error("User data is null")
                    }
                } else {
                    _uiState.value = LoginUiState.Error(response.body()?.message ?: "Invalid credentials")
                }
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}