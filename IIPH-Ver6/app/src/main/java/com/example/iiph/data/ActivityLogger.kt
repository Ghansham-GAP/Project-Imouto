package com.example.iiph.data

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.UUID

object ActivityLogger {

    private val firestore = Firebase.firestore

    fun logActivity(description: String) {
        val activity = Activity(
            id = UUID.randomUUID().toString(),
            description = description
        )
        firestore.collection("activities").document(activity.id).set(activity)
    }
}