package com.example.iiph.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.iiph.ui.components.BottomNavigationBar
import com.example.iiph.ui.home.HomeScreen
import com.example.iiph.ui.screens.NotificationsScreen
import com.example.iiph.ui.screens.ProfileScreen
import com.example.iiph.ui.screens.admin.AdminScreen
import com.example.iiph.ui.screens.auth.AuthScreen
import com.example.iiph.ui.screens.auth.RegistrationCompleteScreen
import com.example.iiph.ui.screens.preparation.PreparationScreen
import com.example.iiph.ui.screens.recruiter.RecruiterScreen
import com.example.iiph.ui.screens.student.StudentScreen

@Composable
fun CampusConnectNavGraph(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val screensWithoutBottomBar = listOf("auth", "registrationComplete/{email}/{course}/{year}")

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (currentDestination?.route !in screensWithoutBottomBar) {
                BottomNavigationBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "auth",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("auth") {
                AuthScreen(navController = navController)
            }
            composable(
                "registrationComplete/{email}/{course}/{year}",
                arguments = listOf(
                    navArgument("email") { type = NavType.StringType },
                    navArgument("course") { type = NavType.StringType },
                    navArgument("year") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val email = backStackEntry.arguments?.getString("email") ?: ""
                val course = backStackEntry.arguments?.getString("course")?.replace("%20", " ") ?: ""
                val year = backStackEntry.arguments?.getString("year")?.replace("%20", " ") ?: ""

                RegistrationCompleteScreen(
                    email = email,
                    course = course,
                    year = year,
                    navController = navController
                )
            }
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
}
