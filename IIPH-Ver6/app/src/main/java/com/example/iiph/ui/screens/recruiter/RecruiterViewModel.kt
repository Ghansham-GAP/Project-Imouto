package com.example.iiph.ui.screens.recruiter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iiph.data.ActivityLogger
import com.example.iiph.data.Application
import com.example.iiph.data.InternshipOpportunity
import com.example.iiph.data.Interview
import com.example.iiph.data.JobPosting
import com.example.iiph.data.PlacementOpportunity
import com.example.iiph.model.User
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

class RecruiterViewModel : ViewModel() {

    private val firestore = Firebase.firestore
    private val auth = Firebase.auth

    private val _jobPostings = MutableStateFlow<List<JobPosting>>(emptyList())
    val jobPostings: StateFlow<List<JobPosting>> = _jobPostings

    private val _totalViews = MutableStateFlow(0)
    val totalViews: StateFlow<Int> = _totalViews

    private val _totalApplicants = MutableStateFlow(0)
    val totalApplicants: StateFlow<Int> = _totalApplicants

    private val _totalShortlisted = MutableStateFlow(0)
    val totalShortlisted: StateFlow<Int> = _totalShortlisted

    private val _applicants = MutableStateFlow<List<User>>(emptyList())
    val applicants: StateFlow<List<User>> = _applicants

    private val _applications = MutableStateFlow<List<Application>>(emptyList())
    val applications: StateFlow<List<Application>> = _applications

    private val _companyName = MutableStateFlow("")

    private val _jobPostedSuccessfully = MutableStateFlow(false)
    val jobPostedSuccessfully = _jobPostedSuccessfully.asStateFlow()

    fun onJobPostedHandled() {
        _jobPostedSuccessfully.value = false
    }

    fun fetchRecruiterData() {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid
            if (userId != null) {
                try {
                    // Fetch current user's company name
                    val userDoc = firestore.collection("users").document(userId).get().await()
                    _companyName.value = userDoc.getString("name") ?: ""

                    // Fetch internship and placement opportunities
                    val internshipSnapshot = firestore.collection("internshipOpportunities").whereEqualTo("recruiterId", userId).get().await()
                    val placementSnapshot = firestore.collection("placementOpportunities").whereEqualTo("recruiterId", userId).get().await()
                    val internships = internshipSnapshot.toObjects(InternshipOpportunity::class.java)
                    val placements = placementSnapshot.toObjects(PlacementOpportunity::class.java)

                    val combinedPostings = internships.map {
                        JobPosting(it.id, it.recruiterId, it.title, it.description, it.company, it.location, it.stipend, "Internship")
                    } + placements.map {
                        JobPosting(it.id, it.recruiterId, it.title, it.description, it.company, it.location, it.stipend, "Placement")
                    }
                    _jobPostings.value = combinedPostings

                    // Fetch applications for the recruiter
                    val applicationsSnapshot = firestore.collection("applications").whereEqualTo("recruiterId", userId).get().await()
                    val applications = applicationsSnapshot.toObjects(Application::class.java)
                    _applications.value = applications

                    // Calculate total applicants
                    _totalApplicants.value = applications.size

                    // Calculate total shortlisted
                    _totalShortlisted.value = applications.count { it.status == "Shortlisted" }

                    // Fetch applicant details
                    val studentIds = applications.map { it.studentId }.distinct()
                    if (studentIds.isNotEmpty()) {
                        val usersSnapshot = firestore.collection("users").whereIn("uid", studentIds).get().await()
                        _applicants.value = usersSnapshot.toObjects(User::class.java)
                    } else {
                        _applicants.value = emptyList()
                    }
                } catch (e: Exception) {
                    // Handle error
                }
            }
        }
    }

    fun postOpportunity(
        title: String,
        description: String,
        location: String,
        stipend: String,
        opportunityType: String
    ) {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid
            if (userId != null) {
                try {
                    val newOpportunityId = UUID.randomUUID().toString()
                    when (opportunityType) {
                        "Internship" -> {
                            val newInternship = InternshipOpportunity(
                                id = newOpportunityId,
                                recruiterId = userId,
                                title = title,
                                description = description,
                                company = _companyName.value,
                                location = location,
                                stipend = stipend,
                                datePosted = Date()
                            )
                            firestore.collection("internshipOpportunities").document(newOpportunityId).set(newInternship).await()
                        }
                        "Placement" -> {
                            val newPlacement = PlacementOpportunity(
                                id = newOpportunityId,
                                recruiterId = userId,
                                title = title,
                                description = description,
                                company = _companyName.value,
                                location = location,
                                stipend = stipend,
                                datePosted = Date()
                            )
                            firestore.collection("placementOpportunities").document(newOpportunityId).set(newPlacement).await()
                        }
                    }
                    ActivityLogger.logActivity("New $opportunityType opportunity posted: $title")

                    // Refresh data after posting
                    fetchRecruiterData()
                    _jobPostedSuccessfully.value = true
                } catch (e: Exception) {
                    // Handle error
                }
            }
        }
    }

    fun scheduleInterview(application: Application) {
        viewModelScope.launch {
            val newInterviewId = UUID.randomUUID().toString()
            val interview = Interview(
                id = newInterviewId,
                applicationId = application.id,
                studentId = application.studentId,
                recruiterId = application.recruiterId,
                date = Date(), // Placeholder date
                location = "Online", // Placeholder location
                notes = "Interview scheduled"
            )
            firestore.collection("interviews").document(newInterviewId).set(interview).await()

            // Update application status
            firestore.collection("applications").document(application.id).update("status", "Interview Scheduled").await()
            fetchRecruiterData() // Refresh data
        }
    }
}