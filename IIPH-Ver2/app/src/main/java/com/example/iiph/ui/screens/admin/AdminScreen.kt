package com.example.iiph.ui.screens.admin

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.iiph.ui.components.AdminQuickActions
import com.example.iiph.ui.components.AnalyticsOverview
import com.example.iiph.ui.components.PlaceholderChart
import com.example.iiph.ui.components.PostingManagementCard
import com.example.iiph.ui.components.UserManagementCard
import com.example.iiph.ui.components.UserSearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(navController: NavHostController) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Dashboard", "Users", "Postings", "Analytics")
    
    Scaffold(
        topBar = {
            AdminTopAppBar(
                title = "Admin Dashboard",
                onBackClick = { navController.popBackStack() },
                onSettingsClick = { /* Open settings */ }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Admin Stats
            AdminStatsCard()
            
            // Tabs
            AdminTabs(selectedTab, tabs) { selectedTab = it }
            
            // Content
            when (selectedTab) {
                0 -> AdminDashboard()
                1 -> UsersManagementTab()
                2 -> PostingsManagementTab()
                3 -> AnalyticsTab()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminTopAppBar(
    title: String,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            IconButton(onClick = onSettingsClick) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
fun AdminStatsCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF06D6A0),
                            Color(0xFF049B6C)
                        )
                    ),
                    shape = MaterialTheme.shapes.large
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Platform Overview",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Total Users: 1,248 • Active Postings: 56",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
                
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⚙️",
                        fontSize = 24.sp
                    )
                }
            }
        }
    }
}

@Composable
fun AdminTabs(selectedTab: Int, tabs: List<String>, onTabSelected: (Int) -> Unit) {
    TabRow(
        selectedTabIndex = selectedTab,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                    )
                }
            )
        }
    }
}

@Composable
fun AdminDashboard() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "System Health",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        
        item {
            SystemHealthGrid()
        }
        
        item {
            Text(
                text = "Recent Activity",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        
        items(5) { index ->
            ActivityLogItem(index)
        }
        
        item {
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        
        item {
            AdminQuickActions()
        }
    }
}

@Composable
fun SystemHealthGrid() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HealthMetricCard(
            title = "Server Status",
            value = "Online",
            status = HealthStatus.GOOD,
            icon = Icons.Default.Cloud,
            modifier = Modifier.weight(1f)
        )
        
        HealthMetricCard(
            title = "Database",
            value = "98%",
            status = HealthStatus.GOOD,
            icon = Icons.Default.Storage,
            modifier = Modifier.weight(1f)
        )
        
        HealthMetricCard(
            title = "API Response",
            value = "124ms",
            status = HealthStatus.WARNING,
            icon = Icons.Default.Speed,
            modifier = Modifier.weight(1f)
        )
    }
}

enum class HealthStatus { GOOD, WARNING, CRITICAL }

@Composable
fun HealthMetricCard(
    title: String,
    value: String,
    status: HealthStatus,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    val statusColor = when (status) {
        HealthStatus.GOOD -> Color(0xFF06D6A0)
        HealthStatus.WARNING -> Color(0xFFFFC107)
        HealthStatus.CRITICAL -> Color(0xFFF72585)
    }
    
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(statusColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = statusColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = statusColor
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ActivityLogItem(index: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val icon = when (index % 3) {
                0 -> Icons.Default.PersonAdd
                1 -> Icons.Default.Work
                else -> Icons.Default.Security
            }
            val iconColor = when (index % 3) {
                0 -> Color(0xFF4361EE)
                1 -> Color(0xFFF72585)
                else -> Color(0xFF06D6A0)
            }
            
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(18.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = when (index % 3) {
                        0 -> "New user registered: TechCorp Solutions"
                        1 -> "Job posted: Software Engineer @ Google"
                        else -> "Recruiter verification completed"
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "2 hours ago",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun UsersManagementTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Search and filters
        UserSearchBar()
        
        // User list with verification status
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(8) { index ->
                UserManagementCard(index)
            }
        }
    }
}

@Composable
fun PostingsManagementTab() {
    // Manage and verify job/internship postings
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(6) { index ->
            PostingManagementCard(index)
        }
    }
}

@Composable
fun AnalyticsTab() {
    // Student performance analytics and reports
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AnalyticsOverview()
        
        // Charts and graphs would go here
        PlaceholderChart("Placement Statistics")
        PlaceholderChart("Student Performance")
        PlaceholderChart("Recruiter Engagement")
    }
}
