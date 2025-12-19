package com.example.iiph.ui.screens.preparation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreparationScreen(navController: NavHostController) {
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Aptitude", "Coding", "Interview", "Resume")
    
    Scaffold(
        topBar = {
            PreparationTopAppBar(
                title = "Preparation Hub",
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Progress Overview
            ProgressOverviewCard()
            
            // Category Chips
            CategoryChips(selectedCategory, categories) { selectedCategory = it }
            
            // Content Grid
            PreparationContentGrid(selectedCategory)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreparationTopAppBar(
    title: String,
    onBackClick: () -> Unit
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
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
fun ProgressOverviewCard() {
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
                            Color(0xFF7209B7),
                            Color(0xFF560BAD)
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
                        text = "Preparation Progress",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "65% Complete • 12/20 Skills Mastered",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    LinearProgressIndicator(
                        progress = 0.65f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        color = Color.White,
                        trackColor = Color.White.copy(alpha = 0.3f)
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
                        text = "📚",
                        fontSize = 24.sp
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryChips(selected: String, categories: List<String>, onCategorySelected: (String) -> Unit) {
    LazyRow(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            FilterChip(
                selected = selected == category,
                onClick = { onCategorySelected(category) },
                label = { Text(category) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = Color.White
                )
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun PreparationContentGrid(selectedCategory: String) {
    val resources = when (selectedCategory) {
        "Aptitude" -> aptitudeResources
        "Coding" -> codingResources
        "Interview" -> interviewResources
        "Resume" -> resumeResources
        else -> allResources
    }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(resources) { resource ->
            ResourceCard(resource)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourceCard(resource: PreparationResource) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { /* Open resource */ },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(resource.color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = resource.icon,
                    contentDescription = resource.title,
                    tint = resource.color,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = resource.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = resource.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Badge(
                        containerColor = resource.color.copy(alpha = 0.1f),
                        contentColor = resource.color
                    ) {
                        Text(resource.category)
                    }
                    
                    if (resource.duration != null) {
                        Text(
                            text = resource.duration,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    if (resource.completed) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Completed",
                            tint = Color(0xFF06D6A0),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Open",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Data Models for Preparation
data class PreparationResource(
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val color: Color,
    val duration: String? = null,
    val completed: Boolean = false
)

val allResources = listOf(
    PreparationResource(
        id = 1,
        title = "Quantitative Aptitude Test",
        description = "50 questions covering arithmetic, algebra, geometry",
        category = "Aptitude",
        icon = Icons.Default.Calculate,
        color = Color(0xFF4361EE),
        duration = "60 mins",
        completed = true
    ),
    PreparationResource(
        id = 2,
        title = "Data Structures Mock Test",
        description = "Practice arrays, linked lists, trees, graphs",
        category = "Coding",
        icon = Icons.Default.Code,
        color = Color(0xFFF72585),
        duration = "90 mins",
        completed = false
    ),
    PreparationResource(
        id = 3,
        title = "Mock Interview - Tech",
        description = "Simulated technical interview with AI feedback",
        category = "Interview",
        icon = Icons.Default.VideoCall,
        color = Color(0xFF7209B7),
        duration = "45 mins",
        completed = false
    ),
    PreparationResource(
        id = 4,
        title = "Resume Builder Pro",
        description = "Create ATS-friendly resumes with templates",
        category = "Resume",
        icon = Icons.Default.Description,
        color = Color(0xFF06D6A0),
        completed = true
    ),
    PreparationResource(
        id = 5,
        title = "Logical Reasoning",
        description = "Puzzles, series, pattern recognition",
        category = "Aptitude",
        icon = Icons.Default.Psychology,
        color = Color(0xFFFF9E00),
        duration = "45 mins",
        completed = false
    )
)

val aptitudeResources = allResources.filter { it.category == "Aptitude" }
val codingResources = allResources.filter { it.category == "Coding" }
val interviewResources = allResources.filter { it.category == "Interview" }
val resumeResources = allResources.filter { it.category == "Resume" }
