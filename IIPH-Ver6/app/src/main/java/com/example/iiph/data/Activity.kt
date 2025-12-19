package com.example.iiph.data

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Activity(
    val id: String = "",
    val description: String = "",
    @ServerTimestamp
    val timestamp: Date? = null
)