package com.example.iiph.data

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class MockInterviewQuestion(
    val id: String = "",
    val category: String = "",
    val question: String = "",
    @ServerTimestamp
    val timestamp: Date? = null
)