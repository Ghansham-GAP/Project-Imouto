package com.example.iiph.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.iiph.ui.home.HomeScreen
import com.example.iiph.ui.login.LoginScreen
import com.example.iiph.ui.profile.ProfileScreen
import com.example.iiph.ui.resume.ResumeBuilderScreen
import com.example.iiph.ui.screens.admin.AdminScreen
import com.example.iiph.ui.screens.auth.AuthViewModel
import com.example.iiph.ui.screens.preparation.MockInterviewScreen
import com.example.iiph.ui.screens.preparation.PreparationScreen
import com.example.iiph.ui.screens.recruiter.RecruiterScreen
import com.example.iiph.ui.screens.student.StudentScreen
import com.example.iiph.ui.signup.SignUpScreen

@Composable
fun CampusConnectNavGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
    snackbarHostState: SnackbarHostState,
    authViewModel: AuthViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "signup"
    ) {
        composable("signup") {
            SignUpScreen(navController = navController, authViewModel = authViewModel)
        }
        composable("login") {
            LoginScreen(navController = navController, authViewModel = authViewModel)
        }
        composable("home") {
            HomeScreen(
                navController = navController,
                snackbarHostState = snackbarHostState,
                authViewModel = authViewModel
            )
        }
        composable("student") {
            StudentScreen(
                navController = navController,
                snackbarHostState = snackbarHostState,
                authViewModel = authViewModel
            )
        }
        composable("recruiter") {
            RecruiterScreen(
                navController = navController,
                snackbarHostState = snackbarHostState,
                authViewModel = authViewModel
            )
        }
        composable("preparation") {
            PreparationScreen(navController = navController)
        }
        composable("admin") {
            AdminScreen(navController = navController)
        }
        composable("profile") {
            ProfileScreen(navController = navController, authViewModel = authViewModel)
        }
        composable("resumeBuilder") {
            ResumeBuilderScreen()
        }
        composable("mockInterview") {
            MockInterviewScreen()
        }
    }
}
