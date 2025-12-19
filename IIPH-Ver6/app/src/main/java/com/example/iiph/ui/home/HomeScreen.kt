package com.example.iiph.ui.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.iiph.data.Module
import com.example.iiph.data.StatItem
import com.example.iiph.data.modules
import com.example.iiph.ui.components.*
import com.example.iiph.ui.screens.auth.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    authViewModel: AuthViewModel
) {
    var selectedModule by remember { mutableStateOf<Module?>(null) }
    val currentUser by authViewModel.currentUser.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // App Bar Section
        item {
            TopAppBar(
                onProfileClick = { navController.navigate("profile") },
                onNotificationClick = { navController.navigate("notifications") }
            )
        }

        // Welcome Header
        item {
            WelcomeCard(
                userName = currentUser?.name ?: "User"
            )
        }

        // Quick Stats
        item {
            QuickStatsSection()
        }

        // Modules Grid
        item {
            Text(
                text = "App Modules",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        // Modules Grid (2x2)
        items(chunkedList(modules, 2)) { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowItems.forEach { module ->
                    ModuleCard(
                        module = module,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            selectedModule = module
                            when (module.id) {
                                1 -> navController.navigate("student")
                                2 -> navController.navigate("recruiter")
                                3 -> navController.navigate("preparation")
                                4 -> navController.navigate("admin")
                            }
                        }
                    )
                }
            }
        }

        // Spacing for bottom bar
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }

    // Module Detail Bottom Sheet
    selectedModule?.let { module ->
        ModuleDetailBottomSheet(
            module = module,
            onDismiss = { selectedModule = null },
            onExplore = {
                selectedModule = null
                when (module.id) {
                    1 -> navController.navigate("student")
                    2 -> navController.navigate("recruiter")
                    3 -> navController.navigate("preparation")
                    4 -> navController.navigate("admin")
                }
            }
        )
    }
}

@Composable
fun TopAppBar(
    onProfileClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icons
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IconButton(
                onClick = onNotificationClick,
                modifier = Modifier.size(40.dp)
            ) {
                BadgedBox(badge = {
                    Badge(
                        modifier = Modifier.offset(x = (-4).dp, y = 4.dp),
                        containerColor = MaterialTheme.colorScheme.error
                    ) {
                        Text("3", fontSize = 10.sp)
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications"
                    )
                }
            }

            IconButton(
                onClick = onProfileClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Profile"
                )
            }
        }
    }
}

@Composable
fun WelcomeCard(
    userName: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF4361EE),
                            Color(0xFF3A0CA3)
                        )
                    ),
                    shape = MaterialTheme.shapes.large
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                        .padding(2.dp)
                ) {
                    // In a real app, use Coil or Glide for image loading
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "User Avatar",
                            tint = Color(0xFF4361EE),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Welcome text and stats
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Welcome back, $userName!",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Quick action button
                IconButton(
                    onClick = { /* Quick action */ },
                    modifier = Modifier.size(40.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.White.copy(alpha = 0.2f)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.RocketLaunch,
                        contentDescription = "Quick Action",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun QuickStatsSection() {
    val stats = listOf(
        StatItem("Applications", "12", Color(0xFF4361EE), Icons.Default.Send),
        StatItem("Interviews", "3", Color(0xFFF72585), Icons.Default.Event)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        stats.forEach { stat ->
            StatCard(stat = stat, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun ModuleCard(
    module: Module,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = module.backgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Icon and Title
            Column {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(module.iconBackgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = module.icon,
                        contentDescription = module.title,
                        tint = module.iconColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = module.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = module.shortDescription,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Action and Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = module.stats,
                    style = MaterialTheme.typography.labelSmall,
                    color = module.iconColor,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Explore",
                    tint = module.iconColor,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

private fun <T> chunkedList(list: List<T>, chunkSize: Int): List<List<T>> {
    return list.chunked(chunkSize)
}
