package com.example.iiph.ui.screens.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun RegistrationCompleteScreen(
    name: String,
    email: String,
    course: String,
    year: String,
    navController: NavHostController
) {
    var animationProgress by remember { mutableStateOf(0f) }
    val rotation by animateFloatAsState(
        targetValue = if (animationProgress == 1f) 360f else 0f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "rotation"
    )

    LaunchedEffect(Unit) {
        // Animate progress
        animate(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1500)
        ) { value, _ ->
            animationProgress = value
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF06D6A0),
                        Color(0xFF049B6C),
                        Color(0xFF028174)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Animated Success Icon
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    // Checkmark animation
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .rotate(rotation)
                            .background(Color.Transparent)
                    ) {
                        if (animationProgress > 0.5f) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Success",
                                tint = Color(0xFF06D6A0),
                                modifier = Modifier.size(60.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Success Message
            Text(
                text = "Account Created Successfully!",
                style = MaterialTheme.typography.displaySmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Welcome to IIPH, $name!",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White.copy(alpha = 0.9f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // User Details Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.9f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DetailRow(
                        icon = Icons.Default.Person,
                        label = "Name",
                        value = name
                    )
                    
                    DetailRow(
                        icon = Icons.Default.Email,
                        label = "Email",
                        value = email
                    )

                    DetailRow(
                        icon = Icons.Default.School,
                        label = "Course",
                        value = course
                    )

                    DetailRow(
                        icon = Icons.Default.CalendarToday,
                        label = "Year",
                        value = year
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    Text(
                        text = "Your profile is 30% complete",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    LinearProgressIndicator(
                        progress = 0.3f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        color = Color(0xFF06D6A0),
                        trackColor = Color(0xFF06D6A0).copy(alpha = 0.2f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Action Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { navController.navigate("home") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    )
                ) {
                    Text(
                        text = "Go to Dashboard",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF06D6A0)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Dashboard",
                        tint = Color(0xFF06D6A0)
                    )
                }

                OutlinedButton(
                    onClick = { navController.navigate("profile") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = MaterialTheme.shapes.large,
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color.White, Color.White)
                        )
                    )
                ) {
                    Text(
                        text = "Complete Your Profile",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Quick Tips
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.2f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "💡 Quick Tips to Get Started:",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TipItem("Complete your profile for better recommendations")
                    TipItem("Upload your resume to apply for opportunities")
                    TipItem("Take aptitude tests to assess your skills")
                    TipItem("Connect with seniors for guidance")
                }
            }
        }
    }
}

@Composable
fun DetailRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFF06D6A0)
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun TipItem(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(4.dp)
                .background(Color.White, CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.9f)
        )
    }
}
