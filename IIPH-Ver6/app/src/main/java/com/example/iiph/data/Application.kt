package com.example.iiph.data

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Application(
    val id: String = "",
    val jobId: String = "",
    val studentId: String = "",
    val recruiterId: String = "",
    val status: String = "Pending",
    @ServerTimestamp
    val dateApplied: Date? = null
)