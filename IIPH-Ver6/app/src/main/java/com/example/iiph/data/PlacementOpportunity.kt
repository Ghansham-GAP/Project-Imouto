package com.example.iiph.data

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class PlacementOpportunity(
    val id: String = "",
    val recruiterId: String = "",
    val title: String = "",
    val description: String = "",
    val company: String = "",
    val location: String = "",
    val stipend: String = "",
    val verified: Boolean = false,
    @ServerTimestamp
    val datePosted: Date? = null
)