package com.example.iiph.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.iiph.ui.home.HomeScreen
import com.example.iiph.ui.login.LoginScreen
import com.example.iiph.ui.screens.ProfileScreen
import com.example.iiph.ui.screens.admin.AdminScreen
import com.example.iiph.ui.screens.preparation.PreparationScreen
import com.example.iiph.ui.screens.recruiter.RecruiterScreen
import com.example.iiph.ui.screens.student.StudentScreen
import com.example.iiph.ui.signup.SignUpScreen

@Composable
fun CampusConnectNavGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
    snackbarHostState: SnackbarHostState
) {
    NavHost(
        navController = navController,
        startDestination = "signup"
    ) {
        composable("signup") {
            SignUpScreen(navController = navController)
        }
        composable("login") {
            LoginScreen(navController = navController)
        }
        composable("home") {
            HomeScreen(navController = navController, snackbarHostState = snackbarHostState)
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
    }
}
