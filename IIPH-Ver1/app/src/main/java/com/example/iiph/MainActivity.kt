package com.example.iiph

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.iiph.data.SessionManager
import com.example.iiph.ui.chat.ChatScreen
import com.example.iiph.ui.chat.ChatViewModelFactory
import com.example.iiph.ui.home.HomeScreen
import com.example.iiph.ui.home.HomeViewModelFactory
import com.example.iiph.ui.login.LoginScreen
import com.example.iiph.ui.resume.ResumeBuilderScreen
import com.example.iiph.ui.resume.ResumeBuilderViewModelFactory
import com.example.iiph.ui.signup.SignUpScreen
import com.example.iiph.ui.theme.IIPHTheme

class MainActivity : ComponentActivity() {
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)
        setContent {
            IIPHTheme {
                AppNavigation(sessionManager, this)
            }
        }
    }
}

@Composable
fun AppNavigation(sessionManager: SessionManager, activity: ComponentActivity) {
    val navController = rememberNavController()
    val startDestination = if (sessionManager.isLoggedIn()) "home" else "login"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            LoginScreen(
                onNavigateToSignUp = { navController.navigate("signup") },
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("signup") {
            SignUpScreen(
                onSignUpSuccess = { navController.navigate("login") },
                onNavigateToLogin = { navController.navigate("login") }
            )
        }
        composable("home") {
            HomeScreen(
                sessionManager = sessionManager, 
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                viewModel = viewModel(factory = HomeViewModelFactory(activity.application)),
                onNavigateToResumeBuilder = { navController.navigate("resumeBuilder") },
                onNavigateToChat = { navController.navigate("chat") }
            )
        }
        composable("resumeBuilder") {
            ResumeBuilderScreen(viewModel = viewModel(factory = ResumeBuilderViewModelFactory(activity.application)))
        }
        composable("chat") {
            ChatScreen(viewModel = viewModel(factory = ChatViewModelFactory(activity.application)))
        }
    }
}