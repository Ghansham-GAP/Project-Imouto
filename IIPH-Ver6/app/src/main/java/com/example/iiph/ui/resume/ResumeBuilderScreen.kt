package com.example.iiph.ui.resume

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ResumeBuilderScreen(
    resumeBuilderViewModel: ResumeBuilderViewModel = viewModel()
) {
    val name by resumeBuilderViewModel.name.collectAsState()
    val email by resumeBuilderViewModel.email.collectAsState()
    val phone by resumeBuilderViewModel.phone.collectAsState()
    val summary by resumeBuilderViewModel.summary.collectAsState()
    val educationList by resumeBuilderViewModel.educationList.collectAsState()
    val experienceList by resumeBuilderViewModel.experienceList.collectAsState()
    val skills by resumeBuilderViewModel.skills.collectAsState()

    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Text("Resume Builder", style = MaterialTheme.typography.headlineMedium) }

        // Personal Details
        item { SectionTitle(title = "Personal Details") }
        item { OutlinedTextField(value = name, onValueChange = { resumeBuilderViewModel.name.value = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(value = email, onValueChange = { resumeBuilderViewModel.email.value = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(value = phone, onValueChange = { resumeBuilderViewModel.phone.value = it }, label = { Text("Phone") }, modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(value = summary, onValueChange = { resumeBuilderViewModel.summary.value = it }, label = { Text("Summary") }, modifier = Modifier.fillMaxWidth()) }

        // Education
        item { SectionTitle(title = "Education") }
        itemsIndexed(educationList) { index, education ->
            Column {
                OutlinedTextField(value = education.school, onValueChange = { resumeBuilderViewModel.onEducationChange(index, education.copy(school = it)) }, label = { Text("School") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = education.degree, onValueChange = { resumeBuilderViewModel.onEducationChange(index, education.copy(degree = it)) }, label = { Text("Degree") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = education.year, onValueChange = { resumeBuilderViewModel.onEducationChange(index, education.copy(year = it)) }, label = { Text("Year") }, modifier = Modifier.fillMaxWidth())
            }
        }
        item { AddItemButton(onClick = { resumeBuilderViewModel.addEducation() }, text = "Add Education") }

        // Experience
        item { SectionTitle(title = "Experience") }
        itemsIndexed(experienceList) { index, experience ->
            Column {
                OutlinedTextField(value = experience.company, onValueChange = { resumeBuilderViewModel.onExperienceChange(index, experience.copy(company = it)) }, label = { Text("Company") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = experience.role, onValueChange = { resumeBuilderViewModel.onExperienceChange(index, experience.copy(role = it)) }, label = { Text("Role") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = experience.duration, onValueChange = { resumeBuilderViewModel.onExperienceChange(index, experience.copy(duration = it)) }, label = { Text("Duration") }, modifier = Modifier.fillMaxWidth())
            }
        }
        item { AddItemButton(onClick = { resumeBuilderViewModel.addExperience() }, text = "Add Experience") }

        // Skills
        item { SectionTitle(title = "Skills") }
        itemsIndexed(skills) { index, skill ->
            OutlinedTextField(value = skill, onValueChange = { resumeBuilderViewModel.onSkillChange(index, it) }, label = { Text("Skill") }, modifier = Modifier.fillMaxWidth())
        }
        item { AddItemButton(onClick = { resumeBuilderViewModel.addSkill() }, text = "Add Skill") }

        // Download Button
        item {
            Button(
                onClick = { showToast(context, "PDF generation not implemented.") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Download as PDF")
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
}

@Composable
private fun AddItemButton(onClick: () -> Unit, text: String) {
    TextButton(onClick = onClick) {
        Icon(Icons.Default.Add, contentDescription = null)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text)
    }
}

private fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
