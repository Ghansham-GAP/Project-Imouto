package com.example.iiph.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class Module(
    val id: Int,
    val title: String,
    val description: String,
    val shortDescription: String,
    val icon: ImageVector,
    val iconColor: Color,
    val iconBackgroundColor: Color,
    val backgroundColor: Color,
    val stats: String,
    val features: List<String>
)

val modules = listOf(
    Module(
        id = 1,
        title = "Student Module",
        description = "Complete student profile management with application tracking, resume builder, and interview scheduling",
        shortDescription = "Profile, applications & tracking",
        icon = Icons.Default.School,
        iconColor = Color(0xFF4361EE),
        iconBackgroundColor = Color(0xFFE7F3FF),
        backgroundColor = Color(0xFFF8FAFF),
        stats = "5 pending tasks",
        features = listOf(
            "Profile Builder",
            "Application Tracker",
            "Resume Generator",
            "Interview Scheduler",
            "Skill Assessment"
        )
    ),
    Module(
        id = 2,
        title = "Recruiter Module",
        description = "Advanced recruitment tools for finding and managing talent, with AI-powered candidate matching",
        shortDescription = "Find talent & manage hires",
        icon = Icons.Default.BusinessCenter,
        iconColor = Color(0xFFF72585),
        iconBackgroundColor = Color(0xFFFFF0F7),
        backgroundColor = Color(0xFFFFFBFF),
        stats = "12 new applicants",
        features = listOf(
            "Candidate Search",
            "AI Matching",
            "Interview Management",
            "Analytics Dashboard",
            "Team Collaboration"
        )
    ),
    Module(
        id = 3,
        title = "Preparation Module",
        description = "Comprehensive preparation resources including mock tests, interview simulations, and skill development",
        shortDescription = "Tests, interviews & skills",
        icon = Icons.Default.Lightbulb,
        iconColor = Color(0xFF7209B7),
        iconBackgroundColor = Color(0xFFF3E8FF),
        backgroundColor = Color(0xFFFBF8FF),
        stats = "3 mock tests pending",
        features = listOf(
            "Mock Tests",
            "Interview Simulator",
            "Skill Courses",
            "Progress Tracking",
            "Personalized Path"
        )
    ),
    Module(
        id = 4,
        title = "Admin Module",
        description = "Complete administrative control with analytics, reporting, and platform management tools",
        shortDescription = "Analytics & platform control",
        icon = Icons.Default.Settings,
        iconColor = Color(0xFF06D6A0),
        iconBackgroundColor = Color(0xFFE0F7FA),
        backgroundColor = Color(0xFFF7FFFE),
        stats = "System monitoring",
        features = listOf(
            "Analytics Dashboard",
            "User Management",
            "Report Generator",
            "Platform Settings",
            "SEO Tools"
        )
    )
)

data class StatItem(
    val title: String,
    val value: String,
    val color: Color,
    val icon: ImageVector
)
