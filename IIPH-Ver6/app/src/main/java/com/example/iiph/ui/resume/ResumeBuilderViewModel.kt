package com.example.iiph.ui.resume

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Education(val school: String = "", val degree: String = "", val year: String = "")
data class Experience(val company: String = "", val role: String = "", val duration: String = "")

class ResumeBuilderViewModel : ViewModel() {

    private val auth = Firebase.auth
    private val firestore = Firebase.firestore

    val name = MutableStateFlow("")
    val email = MutableStateFlow("")
    val phone = MutableStateFlow("")
    val summary = MutableStateFlow("")

    private val _educationList = MutableStateFlow<List<Education>>(listOf(Education()))
    val educationList = _educationList.asStateFlow()

    private val _experienceList = MutableStateFlow<List<Experience>>(listOf(Experience()))
    val experienceList = _experienceList.asStateFlow()

    private val _skills = MutableStateFlow<List<String>>(listOf(""))
    val skills = _skills.asStateFlow()

    fun addEducation() {
        _educationList.value = _educationList.value + Education()
    }

    fun onEducationChange(index: Int, item: Education) {
        val currentList = _educationList.value.toMutableList()
        currentList[index] = item
        _educationList.value = currentList
    }

    fun addExperience() {
        _experienceList.value = _experienceList.value + Experience()
    }

    fun onExperienceChange(index: Int, item: Experience) {
        val currentList = _experienceList.value.toMutableList()
        currentList[index] = item
        _experienceList.value = currentList
    }

    fun addSkill() {
        _skills.value = _skills.value + ""
    }

    fun onSkillChange(index: Int, skill: String) {
        val currentList = _skills.value.toMutableList()
        currentList[index] = skill
        _skills.value = currentList
    }
}