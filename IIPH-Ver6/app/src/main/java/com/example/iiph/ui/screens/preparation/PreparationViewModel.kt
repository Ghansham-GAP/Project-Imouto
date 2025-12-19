package com.example.iiph.ui.screens.preparation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iiph.data.MockTest
import com.example.iiph.data.SkillCourse
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PreparationViewModel : ViewModel() {

    private val firestore = Firebase.firestore

    private val _mockTests = MutableStateFlow<List<MockTest>>(emptyList())
    val mockTests: StateFlow<List<MockTest>> = _mockTests

    private val _skillCourses = MutableStateFlow<List<SkillCourse>>(emptyList())
    val skillCourses: StateFlow<List<SkillCourse>> = _skillCourses

    fun fetchMockTests() {
        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("mockTests").get().await()
                _mockTests.value = snapshot.toObjects(MockTest::class.java)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun fetchSkillCourses() {
        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("skillCourses").get().await()
                _skillCourses.value = snapshot.toObjects(SkillCourse::class.java)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}