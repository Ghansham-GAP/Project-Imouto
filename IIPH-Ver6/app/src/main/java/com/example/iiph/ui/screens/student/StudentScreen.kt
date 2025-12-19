package com.example.iiph.ui.screens.student

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.iiph.data.Application
import com.example.iiph.data.InternshipOpportunity
import com.example.iiph.data.Message
import com.example.iiph.data.PlacementOpportunity
import com.example.iiph.ui.screens.auth.AuthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentScreen(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    authViewModel: AuthViewModel = viewModel(),
    studentViewModel: StudentViewModel = viewModel(),
    messengerViewModel: MessengerViewModel = viewModel()
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Internships", "Placements", "Resources", "Messenger")
    val currentUser by authViewModel.currentUser.collectAsState()
    val internshipOpportunities by studentViewModel.internshipOpportunities.collectAsState()
    val placementOpportunities by studentViewModel.placementOpportunities.collectAsState()
    val applications by studentViewModel.applications.collectAsState()
    val applicationSent by studentViewModel.applicationSent.collectAsState()
    val messages by messengerViewModel.messages.collectAsState()

    LaunchedEffect(Unit) {
        studentViewModel.fetchStudentData()
        messengerViewModel.fetchMessages()
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(applicationSent) {
        if (applicationSent) {
            scope.launch {
                snackbarHostState.showSnackbar("Application sent successfully")
            }
            studentViewModel.onApplicationSentHandled()
        }
    }

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
            StudentStatsOverview(
                name = currentUser?.name ?: "",
                course = currentUser?.course ?: "",
                year = currentUser?.year ?: ""
            )

            // Tabs
            StudentTabs(selectedTab, tabs) { selectedTab = it }

            // Content based on selected tab
            Box(modifier = Modifier.weight(1f)) {
                when (selectedTab) {
                    0 -> InternshipTab(internshipOpportunities, studentViewModel, applications)
                    1 -> PlacementTab(placementOpportunities, studentViewModel, applications)
                    2 -> ResourcesTab(navController)
                    3 -> MessengerTab(messengerViewModel, messages)
                }
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
fun StudentStatsOverview(
    name: String,
    course: String,
    year: String
) {
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
                        text = "$course • Year $year",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = name,
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
                        fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                        softWrap = false
                    )
                }
            )
        }
    }
}

@Composable
fun InternshipTab(internships: List<InternshipOpportunity>, studentViewModel: StudentViewModel, applications: List<Application>) {
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
            OpportunityCard(internship, studentViewModel, applications)
        }
    }
}

@Composable
fun PlacementTab(placements: List<PlacementOpportunity>, studentViewModel: StudentViewModel, applications: List<Application>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Available Placements",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        items(placements) { placement ->
            OpportunityCard(placement, studentViewModel, applications)
        }
    }
}

private data class OpportunityDisplayData(
    val id: String,
    val recruiterId: String,
    val title: String,
    val company: String,
    val location: String,
    val stipend: String
)

@Composable
fun <T> OpportunityCard(opportunity: T, studentViewModel: StudentViewModel, applications: List<Application>) {
    val displayData = when (opportunity) {
        is InternshipOpportunity -> {
            OpportunityDisplayData(
                id = opportunity.id,
                recruiterId = opportunity.recruiterId,
                title = opportunity.title,
                company = opportunity.company,
                location = opportunity.location,
                stipend = opportunity.stipend
            )
        }
        is PlacementOpportunity -> {
            OpportunityDisplayData(
                id = opportunity.id,
                recruiterId = opportunity.recruiterId,
                title = opportunity.title,
                company = opportunity.company,
                location = opportunity.location,
                stipend = opportunity.stipend
            )
        }
        else -> null
    }

    if (displayData == null) return

    val hasApplied = applications.any { it.jobId == displayData.id }

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
            Text(text = displayData.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = displayData.company, style = MaterialTheme.typography.bodyMedium)
            Text(text = displayData.location, style = MaterialTheme.typography.bodyMedium)
            Text(text = "Stipend: ${displayData.stipend}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { studentViewModel.applyForOpportunity(displayData.id, displayData.recruiterId) },
                enabled = !hasApplied
            ) {
                Text(if (hasApplied) "Applied" else "Apply")
            }
        }
    }
}


@Composable
fun ResourcesTab(navController: NavHostController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ResourceCard(
                title = "Resume Builder",
                description = "Create professional resumes with templates",
                icon = Icons.Default.Description,
                color = Color(0xFF4361EE),
                onClick = { navController.navigate("resumeBuilder") }
            )
        }

        item {
            ResourceCard(
                title = "Mock Interviews",
                description = "Practice with AI-powered interview simulator",
                icon = Icons.Default.VideoCall,
                color = Color(0xFFF72585),
                onClick = { navController.navigate("mockInterview") }
            )
        }

        item {
            ResourceCard(
                title = "Aptitude Tests",
                description = "Quantitative, verbal, reasoning practice",
                icon = Icons.Default.Assessment,
                color = Color(0xFF7209B7)
            )
        }

        item {
            ResourceCard(
                title = "Coding Challenges",
                description = "Practice DSA problems with solutions",
                icon = Icons.Default.Code,
                color = Color(0xFF06D6A0)
            )
        }
    }
}

@Composable
fun MessengerTab(messengerViewModel: MessengerViewModel, messages: List<Message>) {
    var message by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Global Messenger",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f),
            reverseLayout = true,
            contentPadding = PaddingValues(bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { msg ->
                MessageCard(msg)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("Type your message") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    if (message.isNotBlank()) {
                        messengerViewModel.postMessage(message)
                        message = ""
                        scope.launch { 
                            listState.animateScrollToItem(0)
                        }
                    }
                },
                enabled = message.isNotBlank()
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send")
            }
        }
    }
}

@Composable
fun MessageCard(message: Message) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${message.name} (${message.email})",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = message.message)
        }
    }
}

@Composable
fun ResourceCard(
    title: String,
    description: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(40.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = description, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
