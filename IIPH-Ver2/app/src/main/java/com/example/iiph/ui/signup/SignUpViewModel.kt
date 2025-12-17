package com.example.iiph.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iiph.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class SignUpUiState {
    object Idle : SignUpUiState()
    object Loading : SignUpUiState()
    object Success : SignUpUiState()
    data class Error(val message: String) : SignUpUiState()
}

class SignUpViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<SignUpUiState>(SignUpUiState.Idle)
    val uiState: StateFlow<SignUpUiState> = _uiState

    fun register(name: String, email: String, pass: String, course: String, year: Int) {
        viewModelScope.launch {
            _uiState.value = SignUpUiState.Loading
            try {
                val response = RetrofitInstance.api.register(name, email, pass, course, year)
                if (response.isSuccessful && response.body()?.status == "success") {
                    _uiState.value = SignUpUiState.Success
                } else {
                    // The request was made, but the server responded with an error.
                    val errorBody = response.errorBody()?.string() ?: "No error body"
                    val responseMessage = response.body()?.message
                    val detailedMessage = "Registration failed. Server says: ${responseMessage ?: errorBody}"
                    _uiState.value = SignUpUiState.Error(detailedMessage)
                }
            } catch (e: Exception) {
                // The request could not be executed (e.g., network error).
                val errorMessage = e.message ?: "An unknown network error occurred"
                _uiState.value = SignUpUiState.Error("Request failed: $errorMessage")
            }
        }
    }
}