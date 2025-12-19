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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.iiph.data.MockTest
import com.example.iiph.data.SkillCourse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreparationScreen(
    navController: NavHostController,
    preparationViewModel: PreparationViewModel = viewModel()
) {
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Aptitude", "Coding", "Interview", "Resume")
    val mockTests by preparationViewModel.mockTests.collectAsState()
    val skillCourses by preparationViewModel.skillCourses.collectAsState()

    preparationViewModel.fetchMockTests()
    preparationViewModel.fetchSkillCourses()

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
            PreparationContentGrid(selectedCategory, mockTests, skillCourses)
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
fun PreparationContentGrid(selectedCategory: String, mockTests: List<MockTest>, skillCourses: List<SkillCourse>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(mockTests) { mockTest ->
            // You can create a composable for mock test card
            Text(text = mockTest.title)
        }
        items(skillCourses) { skillCourse ->
            // You can create a composable for skill course card
            Text(text = skillCourse.title)
        }
    }
}
