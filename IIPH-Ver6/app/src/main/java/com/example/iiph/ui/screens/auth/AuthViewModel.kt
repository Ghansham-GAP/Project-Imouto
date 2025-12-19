package com.example.iiph.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iiph.data.ActivityLogger
import com.example.iiph.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// Represents the different states of the UI
sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val user: User) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth
    private val firestore = Firebase.firestore

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                val authResult = auth.signInWithEmailAndPassword(email, pass).await()
                val firebaseUser = authResult.user
                if (firebaseUser != null) {
                    val user = firestore.collection("users").document(firebaseUser.uid).get().await().toObject(User::class.java)
                    if (user != null) {
                        _currentUser.value = user
                        _uiState.value = AuthUiState.Success(user)
                    } else {
                        _uiState.value = AuthUiState.Error("User data not found in database.")
                    }
                } else {
                     _uiState.value = AuthUiState.Error("Login failed: user not found.")
                }
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.message ?: "An unknown error occurred.")
            }
        }
    }

    fun signup(name: String, email: String, pass: String, course: String, year: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                val authResult = auth.createUserWithEmailAndPassword(email, pass).await()
                val firebaseUser = authResult.user

                if (firebaseUser != null) {
                    val user = User(
                        uid = firebaseUser.uid,
                        name = name,
                        email = email,
                        course = course,
                        year = year
                    )
                    firestore.collection("users").document(firebaseUser.uid).set(user).await()
                    _currentUser.value = user
                    _uiState.value = AuthUiState.Success(user)

                    ActivityLogger.logActivity("New user registered: $name")
                } else {
                    _uiState.value = AuthUiState.Error("Signup succeeded but failed to get user details.")
                }
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.message ?: "An unknown error occurred.")
            }
        }
    }

    fun logout() {
        auth.signOut()
        _currentUser.value = null
        _uiState.value = AuthUiState.Idle
    }
}