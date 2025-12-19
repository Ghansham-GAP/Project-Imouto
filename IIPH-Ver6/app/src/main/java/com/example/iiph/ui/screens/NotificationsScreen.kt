package com.example.iiph.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.iiph.data.Notification

@Composable
fun NotificationsScreen(navController: NavController) {
    val notifications = listOf(
        Notification("New Message", "You have a new message from John Doe"),
        Notification("New Job Posting", "A new job posting is available"),
        Notification("Interview Reminder", "Your interview is tomorrow at 10 AM")
    )

    LazyColumn {
        items(notifications) { notification ->
            Text("Title: ${notification.title}, Message: ${notification.message}")
        }
    }
}