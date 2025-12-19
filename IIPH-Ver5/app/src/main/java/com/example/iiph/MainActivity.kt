package com.example.iiph

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.iiph.navigation.CampusConnectNavGraph
import com.example.iiph.ui.components.BottomNavigationBar
import com.example.iiph.ui.theme.IIPHTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val snackbarHostState = remember { SnackbarHostState() }
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            val screensWithoutBottomBar = listOf("signup", "login")

            IIPHTheme {
                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    bottomBar = {
                        if (currentDestination?.route !in screensWithoutBottomBar) {
                            BottomNavigationBar(navController)
                        }
                    }
                ) { innerPadding ->
                    CampusConnectNavGraph(
                        navController = navController,
                        innerPadding = innerPadding,
                        snackbarHostState = snackbarHostState
                    )
                }
            }
        }
    }
}
