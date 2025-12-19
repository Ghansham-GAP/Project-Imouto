package com.example.iiph.ui.login

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.iiph.ui.screens.auth.AuthUiState
import com.example.iiph.ui.screens.auth.AuthViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    val uiState by authViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Observe the currentUser state to navigate on successful login
    val currentUser by authViewModel.currentUser.collectAsStateWithLifecycle()

    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            navController.navigate("home") {
                // Clear the back stack up to the auth screen
                popUpTo("auth") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { authViewModel.login(email, password) },
            enabled = uiState !is AuthUiState.Loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState is AuthUiState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("Login")
            }
        }

        TextButton(onClick = { navController.navigate("signup") }) {
            Text("Don't have an account? Sign Up")
        }

        // Handle and display login errors
        if (uiState is AuthUiState.Error) {
            val errorMessage = (uiState as AuthUiState.Error).message
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
            LaunchedEffect(errorMessage) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }
}
