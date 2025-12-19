package com.example.iiph.data

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Message(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val message: String = "",
    @ServerTimestamp
    val timestamp: Date? = null
)