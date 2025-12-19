package com.example.iiph.model

import com.google.firebase.firestore.PropertyName

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val course: String = "",
    val year: String = "",

    // Using PropertyName to handle case mismatch between Firestore and Kotlin
    @get:PropertyName("profileComplete") @set:PropertyName("profileComplete") var profileComplete: Int = 0,
    @get:PropertyName("cgpa") @set:PropertyName("cgpa") var cgpa: Double = 0.0
)
