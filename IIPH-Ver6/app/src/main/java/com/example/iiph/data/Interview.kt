package com.example.iiph.data

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Interview(
    val id: String = "",
    val applicationId: String = "",
    val studentId: String = "",
    val recruiterId: String = "",
    val date: Date? = null,
    val location: String = "",
    val notes: String = ""
)