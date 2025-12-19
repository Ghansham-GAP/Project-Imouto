package com.example.iiph.ui.screens.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iiph.data.Message
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class MessengerViewModel : ViewModel() {

    private val firestore = Firebase.firestore
    private val auth = Firebase.auth

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    fun fetchMessages() {
        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("messages")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .limit(50) // Limit to last 50 messages for performance
                    .get()
                    .await()
                _messages.value = snapshot.toObjects(Message::class.java)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun postMessage(message: String) {
        viewModelScope.launch {
            val firebaseUser = auth.currentUser
            if (firebaseUser != null) {
                try {
                    // Fetch user details from "users" collection
                    val userDoc = firestore.collection("users").document(firebaseUser.uid).get().await()
                    val userName = userDoc.getString("name") ?: "Anonymous"
                    val userEmail = firebaseUser.email ?: ""

                    val newMessage = Message(
                        id = UUID.randomUUID().toString(),
                        name = userName,
                        email = userEmail,
                        message = message
                    )
                    firestore.collection("messages").document(newMessage.id).set(newMessage).await()
                    fetchMessages() // Refresh messages after posting
                } catch (e: Exception) {
                    // Handle error
                }
            }
        }
    }
}