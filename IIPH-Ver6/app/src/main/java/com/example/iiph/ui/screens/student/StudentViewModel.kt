package com.example.iiph.ui.screens.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iiph.data.Application
import com.example.iiph.data.InternshipOpportunity
import com.example.iiph.data.PlacementOpportunity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Date
import java.util.UUID

class StudentViewModel : ViewModel() {

    private val firestore = Firebase.firestore
    private val auth = Firebase.auth

    private val _applications = MutableStateFlow<List<Application>>(emptyList())
    val applications: StateFlow<List<Application>> = _applications

    private val _internshipOpportunities = MutableStateFlow<List<InternshipOpportunity>>(emptyList())
    val internshipOpportunities: StateFlow<List<InternshipOpportunity>> = _internshipOpportunities

    private val _placementOpportunities = MutableStateFlow<List<PlacementOpportunity>>(emptyList())
    val placementOpportunities: StateFlow<List<PlacementOpportunity>> = _placementOpportunities

    private val _applicationSent = MutableStateFlow(false)
    val applicationSent = _applicationSent.asStateFlow()

    fun onApplicationSentHandled() {
        _applicationSent.value = false
    }

    fun fetchStudentData() {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid
            if (userId != null) {
                try {
                    // Fetch applications
                    val snapshot = firestore.collection("applications")
                        .whereEqualTo("studentId", userId)
                        .get()
                        .await()
                    _applications.value = snapshot.toObjects(Application::class.java)

                    // Fetch internship opportunities, filtering out self-posted ones
                    val internshipSnapshot = firestore.collection("internshipOpportunities").get().await()
                    _internshipOpportunities.value = internshipSnapshot.toObjects(InternshipOpportunity::class.java)
                        .filter { it.recruiterId != userId }

                    // Fetch placement opportunities, filtering out self-posted ones
                    val placementSnapshot = firestore.collection("placementOpportunities").get().await()
                    _placementOpportunities.value = placementSnapshot.toObjects(PlacementOpportunity::class.java)
                        .filter { it.recruiterId != userId }

                } catch (e: Exception) {
                    // Handle error
                }
            }
        }
    }

    fun applyForOpportunity(opportunityId: String, recruiterId: String) {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid
            if (userId != null) {
                try {
                    val newApplicationId = UUID.randomUUID().toString()
                    val newApplication = Application(
                        id = newApplicationId,
                        jobId = opportunityId,
                        studentId = userId,
                        recruiterId = recruiterId,
                        status = "Pending",
                        dateApplied = Date()
                    )
                    firestore.collection("applications").document(newApplicationId).set(newApplication).await()
                    _applicationSent.value = true
                    fetchStudentData()
                } catch (e: Exception) {
                    // Handle error
                }
            }
        }
    }
}