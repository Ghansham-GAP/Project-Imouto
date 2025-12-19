package com.example.iiph.ui.screens.recruiter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.iiph.data.ActionItem
import com.example.iiph.data.Application
import com.example.iiph.data.JobPosting
import com.example.iiph.data.StatItem
import com.example.iiph.model.User
import com.example.iiph.ui.components.*
import com.example.iiph.ui.screens.auth.AuthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecruiterScreen(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    authViewModel: AuthViewModel = viewModel(),
    recruiterViewModel: RecruiterViewModel = viewModel()
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Dashboard", "Post Jobs", "Candidates", "Interviews")
    val currentUser by authViewModel.currentUser.collectAsState()
    val jobPostings by recruiterViewModel.jobPostings.collectAsState()
    val totalViews by recruiterViewModel.totalViews.collectAsState()
    val totalApplicants by recruiterViewModel.totalApplicants.collectAsState()
    val totalShortlisted by recruiterViewModel.totalShortlisted.collectAsState()
    val applicants by recruiterViewModel.applicants.collectAsState()
    val applications by recruiterViewModel.applications.collectAsState()
    val jobPostedSuccessfully by recruiterViewModel.jobPostedSuccessfully.collectAsState()

    LaunchedEffect(Unit) {
        recruiterViewModel.fetchRecruiterData()
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(jobPostedSuccessfully) {
        if (jobPostedSuccessfully) {
            scope.launch {
                snackbarHostState.showSnackbar("Opportunity posted successfully")
            }
            recruiterViewModel.onJobPostedHandled()
        }
    }

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
            CompanyOverviewCard(currentUser?.name ?: "", jobPostings.size)

            // Tabs
            RecruiterTabs(selectedTab, tabs) { selectedTab = it }

            // Content
            when (selectedTab) {
                0 -> RecruiterDashboard(totalViews, totalApplicants, totalShortlisted, applicants)
                1 -> PostJobsTab(jobPostings, recruiterViewModel)
                2 -> CandidatesTab(applicants)
                3 -> InterviewsTab(applications, recruiterViewModel)
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
fun CompanyOverviewCard(name: String, activePostings: Int) {
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
                        text = name,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Verified Partner • $activePostings Active Postings",
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
fun RecruiterDashboard(
    totalViews: Int,
    totalApplicants: Int,
    totalShortlisted: Int,
    applicants: List<User>
) {
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
            RecruitmentStatsRow(totalViews, totalApplicants, totalShortlisted)
        }

        item {
            Text(
                text = "Recent Applications",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        items(applicants) { applicant ->
            ApplicationCard(applicant)
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
fun RecruitmentStatsRow(totalViews: Int, totalApplicants: Int, totalShortlisted: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            stat = StatItem(
                title = "Total Views",
                value = totalViews.toString(),
                icon = Icons.Default.Visibility,
                color = Color(0xFFF72585)
            ),
            modifier = Modifier.weight(1f)
        )

        StatCard(
            stat = StatItem(
                title = "Applicants",
                value = totalApplicants.toString(),
                icon = Icons.Default.People,
                color = Color(0xFF4361EE)
            ),
            modifier = Modifier.weight(1f)
        )

        StatCard(
            stat = StatItem(
                title = "Shortlisted",
                value = totalShortlisted.toString(),
                icon = Icons.Default.Star,
                color = Color(0xFF06D6A0)
            ),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ApplicationCard(applicant: User) {
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
                    text = applicant.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${applicant.course} • Year ${applicant.year}",
                    style = MaterialTheme.typography.bodySmall,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostJobsTab(jobPostings: List<JobPosting>, recruiterViewModel: RecruiterViewModel) {
    var jobTitle by remember { mutableStateOf("") }
    var jobDescription by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var stipend by remember { mutableStateOf("") }
    val opportunityTypes = listOf("Internship", "Placement")
    var selectedOpportunityType by remember { mutableStateOf(opportunityTypes[0]) }

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
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") },
                    modifier = Modifier.weight(1f),
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) }
                )

                OutlinedTextField(
                    value = stipend,
                    onValueChange = { stipend = it },
                    label = { Text("Stipend/Salary") },
                    modifier = Modifier.weight(1f),
                    leadingIcon = { Text("₹", fontSize = 20.sp, modifier = Modifier.padding(start = 8.dp)) }
                )
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth()) {
                opportunityTypes.forEach {
                    Row(
                        Modifier
                            .selectable(
                                selected = (it == selectedOpportunityType),
                                onClick = { selectedOpportunityType = it }
                            )
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        RadioButton(
                            selected = (it == selectedOpportunityType),
                            onClick = { selectedOpportunityType = it }
                        )
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }

        item {
            Button(
                onClick = {
                    recruiterViewModel.postOpportunity(
                        title = jobTitle,
                        description = jobDescription,
                        location = location,
                        stipend = stipend,
                        opportunityType = selectedOpportunityType
                    )
                },
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

        items(jobPostings) { jobPosting ->
            JobPostingCard(jobPosting)
        }
    }
}

@Composable
fun CandidatesTab(applicants: List<User>) {
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
            items(applicants) { applicant ->
                CandidateListItem(applicant)
            }
        }
    }
}

@Composable
fun InterviewsTab(applications: List<Application>, recruiterViewModel: RecruiterViewModel) {
    // Interview scheduling and management
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Upcoming Interviews",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        items(applications) { application ->
            InterviewCard(application, recruiterViewModel)
        }
    }
}

@Composable
fun JobPostingCard(jobPosting: JobPosting) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Text(text = jobPosting.title, modifier = Modifier.padding(16.dp))
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
fun CandidateListItem(applicant: User) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Text(text = applicant.name, modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun InterviewCard(application: Application, recruiterViewModel: RecruiterViewModel) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Application ID: ${application.id}", fontWeight = FontWeight.Bold)
                Text(text = "Student ID: ${application.studentId}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Status: ${application.status}", style = MaterialTheme.typography.bodySmall)
            }
            Button(onClick = { recruiterViewModel.scheduleInterview(application) }) {
                Text("Schedule")
            }
        }
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
