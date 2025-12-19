package com.example.iiph.ui.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iiph.data.Activity
import com.example.iiph.data.InternshipOpportunity
import com.example.iiph.data.PlacementOpportunity
import com.example.iiph.model.User
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AdminViewModel : ViewModel() {

    private val firestore = Firebase.firestore

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    private val _activities = MutableStateFlow<List<Activity>>(emptyList())
    val activities: StateFlow<List<Activity>> = _activities

    private val _internshipOpportunities = MutableStateFlow<List<InternshipOpportunity>>(emptyList())
    val internshipOpportunities: StateFlow<List<InternshipOpportunity>> = _internshipOpportunities

    private val _placementOpportunities = MutableStateFlow<List<PlacementOpportunity>>(emptyList())
    val placementOpportunities: StateFlow<List<PlacementOpportunity>> = _placementOpportunities

    fun fetchAdminData() {
        fetchUsers()
        fetchActivities()
        fetchInternshipOpportunities()
        fetchPlacementOpportunities()
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("users").get().await()
                _users.value = snapshot.toObjects(User::class.java)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    private fun fetchActivities() {
        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("activities")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .limit(10)
                    .get()
                    .await()
                _activities.value = snapshot.toObjects(Activity::class.java)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    private fun fetchInternshipOpportunities() {
        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("internshipOpportunities").get().await()
                _internshipOpportunities.value = snapshot.toObjects(InternshipOpportunity::class.java)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    private fun fetchPlacementOpportunities() {
        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("placementOpportunities").get().await()
                _placementOpportunities.value = snapshot.toObjects(PlacementOpportunity::class.java)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun verifyOpportunity(opportunity: Any) {
        viewModelScope.launch {
            when (opportunity) {
                is InternshipOpportunity -> {
                    firestore.collection("internshipOpportunities").document(opportunity.id).update("verified", true).await()
                    fetchInternshipOpportunities()
                }
                is PlacementOpportunity -> {
                    firestore.collection("placementOpportunities").document(opportunity.id).update("verified", true).await()
                    fetchPlacementOpportunities()
                }
            }
        }
    }

    fun removeOpportunity(opportunity: Any) {
        viewModelScope.launch {
            when (opportunity) {
                is InternshipOpportunity -> {
                    firestore.collection("internshipOpportunities").document(opportunity.id).delete().await()
                    fetchInternshipOpportunities()
                }
                is PlacementOpportunity -> {
                    firestore.collection("placementOpportunities").document(opportunity.id).delete().await()
                    fetchPlacementOpportunities()
                }
            }
        }
    }
}