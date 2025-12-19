package com.example.iiph.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iiph.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    init {
        fetchUser()
    }

    private fun fetchUser() {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid
            if (userId != null) {
                firestore.collection("users").document(userId).get()
                    .addOnSuccessListener {
                        _user.value = it.toObject(User::class.java)
                    }
            }
        }
    }
}