package com.example.iiph.ui.screens.recruiter

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.iiph.data.ActionItem
import com.example.iiph.data.StatItem
import com.example.iiph.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecruiterScreen(navController: NavHostController) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Dashboard", "Post Jobs", "Candidates", "Interviews")
    
    Scaffold(
        topBar = {
            RecruiterTopAppBar(
                title = "Recruiter Portal",
                onBackClick = { navController.popBackStack() },
                onAddClick = { /* Post new job */ }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Company Overview
            CompanyOverviewCard()
            
            // Tabs
            RecruiterTabs(selectedTab, tabs) { selectedTab = it }
            
            // Content
            when (selectedTab) {
                0 -> RecruiterDashboard()
                1 -> PostJobsTab()
                2 -> CandidatesTab()
                3 -> InterviewsTab()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecruiterTopAppBar(
    title: String,
    onBackClick: () -> Unit,
    onAddClick: () -> Unit
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
            IconButton(onClick = onAddClick) {
                Icon(Icons.Default.PostAdd, contentDescription = "Post Job")
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
fun CompanyOverviewCard() {
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
                            Color(0xFFF72585),
                            Color(0xFFB5179E)
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
                        text = "TechCorp Solutions",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Verified Partner • 5 Active Postings",
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
                        text = "🏢",
                        fontSize = 24.sp
                    )
                }
            }
        }
    }
}

@Composable
fun RecruiterTabs(selectedTab: Int, tabs: List<String>, onTabSelected: (Int) -> Unit) {
    TabRow(
        selectedTabIndex = selectedTab,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.secondary,
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
fun RecruiterDashboard() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Recruitment Analytics",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        
        item {
            RecruitmentStatsRow()
        }
        
        item {
            Text(
                text = "Recent Applications",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        
        items(4) { index ->
            ApplicationCard(index)
        }
        
        item {
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        
        item {
            QuickActionsGrid()
        }
    }
}

@Composable
fun RecruitmentStatsRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            stat = StatItem(
                title = "Total Views",
                value = "1.2K",
                icon = Icons.Default.Visibility,
                color = Color(0xFFF72585)
            ),
            modifier = Modifier.weight(1f)
        )
        
        StatCard(
            stat = StatItem(
                title = "Applicants",
                value = "48",
                icon = Icons.Default.People,
                color = Color(0xFF4361EE)
            ),
            modifier = Modifier.weight(1f)
        )
        
        StatCard(
            stat = StatItem(
                title = "Shortlisted",
                value = "12",
                icon = Icons.Default.Star,
                color = Color(0xFF06D6A0)
            ),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ApplicationCard(index: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(Color(0xFFE7F3FF)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "👨‍💻",
                    fontSize = 20.sp
                )
            }
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = if (index % 2 == 0) "Alex Johnson" else "Sarah Chen",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Computer Science • Year 3",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "CGPA: ${if (index % 2 == 0) "8.7" else "9.2"}/10",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Badge(
                    containerColor = when (index % 3) {
                        0 -> Color(0xFF4CC9F0).copy(alpha = 0.2f)
                        1 -> Color(0xFF06D6A0).copy(alpha = 0.2f)
                        else -> Color(0xFFFFC107).copy(alpha = 0.2f)
                    },
                    contentColor = when (index % 3) {
                        0 -> Color(0xFF4CC9F0)
                        1 -> Color(0xFF06D6A0)
                        else -> Color(0xFFFFC107)
                    }
                ) {
                    Text(
                        text = when (index % 3) {
                            0 -> "NEW"
                            1 -> "SHORTLISTED"
                            else -> "REVIEW"
                        },
                        fontSize = 10.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "2 days ago",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun QuickActionsGrid() {
    val actions = listOf<ActionItem>(
        ActionItem("Filter Candidates", Icons.Default.FilterList, Color(0xFF4361EE)),
        ActionItem("Schedule Interview", Icons.Default.CalendarToday, Color(0xFFF72585)),
        ActionItem("Send Bulk Email", Icons.Default.Email, Color(0xFF7209B7)),
        ActionItem("Download Reports", Icons.Default.Download, Color(0xFF06D6A0))
    )
    
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        actions.chunked(2).forEach { rowActions ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowActions.forEach { action ->
                    QuickActionCard(action, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickActionCard(action: ActionItem, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        onClick = { /* Handle action */ },
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
                    .background(action.color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = action.icon,
                    contentDescription = action.title,
                    tint = action.color,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = action.title,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun PostJobsTab() {
    var jobTitle by remember { mutableStateOf("") }
    var jobDescription by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Technology") }
    var isRemote by remember { mutableStateOf(false) }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Post New Opportunity",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        
        item {
            OutlinedTextField(
                value = jobTitle,
                onValueChange = { jobTitle = it },
                label = { Text("Job Title") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Work, contentDescription = null) }
            )
        }
        
        item {
            OutlinedTextField(
                value = jobDescription,
                onValueChange = { jobDescription = it },
                label = { Text("Job Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                leadingIcon = { Icon(Icons.Default.Description, contentDescription = null) }
            )
        }
        
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("Location") },
                    modifier = Modifier.weight(1f),
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) }
                )
                
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("Stipend/Salary") },
                    modifier = Modifier.weight(1f),
                    leadingIcon = { Icon(Icons.Default.AttachMoney, contentDescription = null) }
                )
            }
        }
        
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterChip(
                    selected = isRemote,
                    onClick = { isRemote = !isRemote },
                    label = { Text("Remote") },
                    leadingIcon = if (isRemote) {
                        { Icon(Icons.Default.Check, contentDescription = null) }
                    } else null
                )
                
                FilterChip(
                    selected = selectedCategory == "Technology",
                    onClick = { selectedCategory = "Technology" },
                    label = { Text("Technology") }
                )
                
                FilterChip(
                    selected = selectedCategory == "Business",
                    onClick = { selectedCategory = "Business" },
                    label = { Text("Business") }
                )
            }
        }
        
        item {
            Button(
                onClick = { /* Post job */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(Icons.Default.PostAdd, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Post Opportunity")
            }
        }
        
        item {
            Divider()
        }
        
        item {
            Text(
                text = "Your Active Postings",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        
        items(3) { index ->
            JobPostingCard(index)
        }
    }
}

@Composable
fun CandidatesTab() {
    // Candidate search and filtering
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Search and filters
        CandidateSearchBar()
        
        // Candidate list
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(10) { index ->
                CandidateListItem(index)
            }
        }
    }
}

@Composable
fun InterviewsTab() {
    // Interview scheduling and management
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Upcoming Interviews",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        
        repeat(4) { index ->
            InterviewCard(index)
        }
    }
}

@Composable
fun JobPostingCard(index: Int) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Job posting ${index + 1}", modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun CandidateSearchBar() {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        label = { Text("Search candidates...") },
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
    )
}

@Composable
fun CandidateListItem(index: Int) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Candidate ${index + 1}", modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun InterviewCard(index: Int) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Interview ${index + 1}", modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun StatCard(stat: StatItem, modifier: Modifier) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stat.title, style = MaterialTheme.typography.labelMedium)
            Text(text = stat.value, style = MaterialTheme.typography.headlineSmall)
        }
    }
}
