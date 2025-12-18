package com.example.iiph.ui.screens.student

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.iiph.ui.components.*

// Data classes moved here to resolve build errors
data class Internship(
    val id: Int,
    val title: String,
    val company: String,
    val location: String,
    val duration: String,
    val stipend: String,
    val deadline: String,
    val category: String,
    val applied: Boolean
)

data class InternshipFeedback(
    val studentName: String,
    val company: String,
    val role: String,
    val rating: Float,
    val feedback: String,
    val year: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentScreen(navController: NavHostController) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Internships", "Placements", "Resources", "Messenger")
    
    Scaffold(
        topBar = {
            StudentTopAppBar(
                title = "Student Dashboard",
                onBackClick = { navController.popBackStack() },
                onProfileClick = { navController.navigate("profile") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Stats Overview
            StudentStatsOverview()
            
            // Tabs
            StudentTabs(selectedTab, tabs) { selectedTab = it }
            
            // Content based on selected tab
            when (selectedTab) {
                0 -> InternshipTab()
                1 -> PlacementTab()
                2 -> ResourcesTab()
                3 -> MessengerTab()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentTopAppBar(
    title: String,
    onBackClick: () -> Unit,
    onProfileClick: () -> Unit
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
            IconButton(onClick = onProfileClick) {
                Icon(Icons.Default.AccountCircle, contentDescription = "Profile")
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
fun StudentStatsOverview() {
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
                .height(140.dp)
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
                    .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Computer Science • Year 3",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Alex Johnson",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "CGPA: 8.7/10 • 85% Profile Complete",
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
                        text = "👨‍🎓",
                        fontSize = 24.sp
                    )
                }
            }
        }
    }
}

@Composable
fun StudentTabs(selectedTab: Int, tabs: List<String>, onTabSelected: (Int) -> Unit) {
    TabRow(
        selectedTabIndex = selectedTab,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary,
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
fun InternshipTab() {
    val internships = listOf<Internship>(
        Internship(
            id = 1,
            title = "Software Development Intern",
            company = "Google",
            location = "Mountain View, CA",
            duration = "3 months",
            stipend = "$6,000/month",
            deadline = "2024-03-15",
            category = "Technology",
            applied = true
        ),
        Internship(
            id = 2,
            title = "Data Science Intern",
            company = "Microsoft",
            location = "Remote",
            duration = "6 months",
            stipend = "$5,500/month",
            deadline = "2024-04-01",
            category = "Data",
            applied = false
        )
    )
    
    val feedbacks = listOf<InternshipFeedback>(
        InternshipFeedback(
            studentName = "Sarah Chen",
            company = "Amazon",
            role = "SDE Intern",
            rating = 4.5f,
            feedback = "Great learning experience with excellent mentorship. Worked on real AWS projects.",
            year = "2023"
        )
    )
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Available Internships",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        
        items(internships) { internship ->
            InternshipCard(internship)
        }
        
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Senior Feedback",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        
        items(feedbacks) { feedback ->
            FeedbackCard(feedback)
        }
    }
}

@Composable
fun InternshipCard(internship: Internship) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = internship.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = internship.company,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                if (internship.applied) {
                    Badge(
                        containerColor = Color(0xFF4CC9F0).copy(alpha = 0.2f),
                        contentColor = Color(0xFF4CC9F0)
                    ) {
                        Text("Applied")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InfoChip(icon = Icons.Default.LocationOn, text = internship.location)
                InfoChip(icon = Icons.Default.Schedule, text = internship.duration)
                InfoChip(icon = Icons.Default.AttachMoney, text = internship.stipend)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Deadline: ${internship.deadline}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )
                
                Button(
                    onClick = { /* Handle apply */ },
                    enabled = !internship.applied,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (internship.applied) MaterialTheme.colorScheme.surfaceVariant 
                        else MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(if (internship.applied) "Applied" else "Apply Now")
                }
            }
        }
    }
}

@Composable
fun FeedbackCard(feedback: InternshipFeedback) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${feedback.studentName} • ${feedback.year}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${feedback.role} @ ${feedback.company}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${feedback.rating}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = feedback.feedback,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Button(
                onClick = { /* Connect with senior */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Message,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Connect with ${feedback.studentName}")
            }
        }
    }
}

// Other tabs implementations follow similar pattern
@Composable
fun PlacementTab() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Placement Updates",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        
        item {
            LiveUpdatesCard()
        }
        
        item {
            Text(
                text = "Your Applications",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        
        items(3) { index ->
            PlacementApplicationCard(index)
        }
    }
}

@Composable
fun LiveUpdatesCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LiveTv,
                    contentDescription = "Live",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "LIVE UPDATES",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "• Google campus drive: Technical interviews ongoing in Block A",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "• Microsoft: Final shortlist announced - Check portal",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "• Amazon: Pre-placement talk tomorrow at 2 PM",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun ResourcesTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ResourceCard(
            title = "Resume Builder",
            description = "Create professional resumes with templates",
            icon = Icons.Default.Description,
            color = Color(0xFF4361EE)
        )
        
        ResourceCard(
            title = "Mock Interviews",
            description = "Practice with AI-powered interview simulator",
            icon = Icons.Default.VideoCall,
            color = Color(0xFFF72585)
        )
        
        ResourceCard(
            title = "Aptitude Tests",
            description = "Quantitative, verbal, reasoning practice",
            icon = Icons.Default.Assessment,
            color = Color(0xFF7209B7)
        )
        
        ResourceCard(
            title = "Coding Challenges",
            description = "Practice DSA problems with solutions",
            icon = Icons.Default.Code,
            color = Color(0xFF06D6A0)
        )
    }
}

@Composable
fun MessengerTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Connect with Peers & Seniors",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        
        // Recent chats
        RecentChatsList()
        
        // Start new conversation
        Button(
            onClick = { /* Start new chat */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Start New Conversation")
        }
    }
}

@Composable
fun RecentChatsList() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(5) { index ->
            ChatItemCard(
                name = if (index % 2 == 0) "Sarah Chen (Senior)" else "Mike Ross (Peer)",
                lastMessage = if (index % 2 == 0) "Can you share your internship experience?" 
                else "Let's practice for the coding round",
                time = "2:30 PM",
                unread = index == 0
            )
        }
    }
}