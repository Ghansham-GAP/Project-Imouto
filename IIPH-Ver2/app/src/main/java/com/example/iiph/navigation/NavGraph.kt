package com.example.iiph.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.iiph.ui.home.HomeScreen
import com.example.iiph.ui.screens.NotificationsScreen
import com.example.iiph.ui.screens.ProfileScreen
import com.example.iiph.ui.screens.admin.AdminScreen
import com.example.iiph.ui.screens.preparation.PreparationScreen
import com.example.iiph.ui.screens.recruiter.RecruiterScreen
import com.example.iiph.ui.screens.student.StudentScreen

@Composable
fun CampusConnectNavGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") {
            HomeScreen(
                navController = navController,
                snackbarHostState = snackbarHostState
            )
        }
        composable("student") {
            StudentScreen(navController = navController)
        }
        composable("recruiter") {
            RecruiterScreen(navController = navController)
        }
        composable("preparation") {
            PreparationScreen(navController = navController)
        }
        composable("admin") {
            AdminScreen(navController = navController)
        }
        composable("profile") {
            ProfileScreen(navController = navController)
        }
        composable("notifications") {
            NotificationsScreen(navController = navController)
        }
    }
}