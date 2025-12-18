package com.example.iiph.ui.screens.auth

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.iiph.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AuthScreen(navController: NavHostController) {
    var isLoginScreen by remember { mutableStateOf(true) }
    val formElevation by animateDpAsState(
        targetValue = if (isLoginScreen) 8.dp else 16.dp,
        label = "formElevation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF4361EE),
                        Color(0xFF3A0CA3),
                        Color(0xFF7209B7)
                    ),
                    startY = 0f,
                    endY = 800f
                )
            )
    ) {
        // Background pattern
        Image(
            painter = painterResource(id = R.drawable.auth_pattern),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.1f
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Section
            AuthHeader(isLoginScreen)
            
            Spacer(modifier = Modifier.height(32.dp))

            // Form Card
            AuthFormCard(
                isLoginScreen = isLoginScreen,
                elevation = formElevation,
                onToggleScreen = { isLoginScreen = !isLoginScreen },
                onLoginSuccess = { navController.navigate("home") },
                onSignupSuccess = { navController.navigate("registrationComplete/student@university.edu/Computer%20Science/3rd%20Year") }
            )

            Spacer(modifier = Modifier.height(32.dp))
            
            // Footer with quick actions
            AuthFooter()
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun AuthHeader(isLoginScreen: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Animated Logo with gradient
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.3f),
                            Color.White.copy(alpha = 0.1f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            // Graduation cap icon using text
            Text(
                text = "🎓",
                fontSize = 48.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = if (isLoginScreen) "Welcome Back!" else "Join IIPH",
            style = MaterialTheme.typography.displaySmall,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = if (isLoginScreen)
                "Sign in to continue your journey"
            else
                "Start your placement journey today",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White.copy(alpha = 0.9f),
            modifier = Modifier.padding(top = 8.dp)
        )

        // Animated indicator dots
        Row(
            modifier = Modifier.padding(top = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(3) { index ->
                Box(
                    modifier = Modifier
                        .size(if (index == 0) 10.dp else 8.dp)
                        .background(
                            color = if (index == 0) Color.White else Color.White.copy(alpha = 0.3f),
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AuthFormCard(
    isLoginScreen: Boolean,
    elevation: androidx.compose.ui.unit.Dp,
    onToggleScreen: () -> Unit,
    onLoginSuccess: () -> Unit,
    onSignupSuccess: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 40.dp),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            if (isLoginScreen) {
                LoginForm(
                    onLoginSuccess = onLoginSuccess,
                    onSwitchToSignup = onToggleScreen
                )
            } else {
                SignupForm(
                    onSignupSuccess = onSignupSuccess,
                    onSwitchToLogin = onToggleScreen
                )
            }

            // Divider with OR
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )
                Text(
                    text = "OR",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline
                )
                Divider(
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )
            }

            // Social Login
            SocialLoginButtons()

            // Toggle between Login/Signup
            TextButton(
                onClick = onToggleScreen,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (isLoginScreen)
                        "Don't have an account? Sign Up"
                    else
                        "Already have an account? Sign In",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginForm(
    onLoginSuccess: () -> Unit,
    onSwitchToSignup: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(true) }

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Email Field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(Icons.Default.Email, contentDescription = "Email")
            },
            singleLine = true,
            shape = MaterialTheme.shapes.large,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
        )

        // Password Field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = "Password")
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility
                                     else Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisible) "Hide password"
                                            else "Show password"
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None
                                 else PasswordVisualTransformation(),
            singleLine = true,
            shape = MaterialTheme.shapes.large,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
        )

        // Remember Me & Forgot Password
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary
                    )
                )
                Text(
                    text = "Remember me",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            TextButton(onClick = { /* Forgot password */ }) {
                Text(
                    text = "Forgot Password?",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Login Button
        Button(
            onClick = {
                isLoading = true
                // Simulate login process
                // In real app, call API here
                onLoginSuccess()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            enabled = email.isNotBlank() && password.isNotBlank() && !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Sign In",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Sign In"
                )
            }
        }

        // Quick Signup
        TextButton(
            onClick = onSwitchToSignup,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("New to campus? Create account")
        }
    }
}
// Add this extension function for better course selection
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CourseYearSelection(
    selectedCourse: String,
    selectedYear: String,
    onCourseSelected: (String) -> Unit,
    onYearSelected: (String) -> Unit
) {
    val courses = listOf(
        "Computer Science",
        "Information Technology",
        "Electronics (ENTC)",
        "Mechanical",
        "Civil",
        "Chemical",
        "Electrical",
        "Biotechnology"
    )
    var isCourseExpanded by remember { mutableStateOf(false) }

    val years = listOf("1st Year", "2nd Year", "3rd Year", "4th Year")

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // Course Dropdown
        ExposedDropdownMenuBox(
            expanded = isCourseExpanded,
            onExpandedChange = {isCourseExpanded = it},
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedCourse,
                onValueChange = {},
                label = { Text("Select Your Course") },
                placeholder = { Text("e.g., Computer Science") },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                leadingIcon = {
                    Icon(Icons.Default.School, contentDescription = "Course")
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCourseExpanded)
                },
                readOnly = true,
                shape = MaterialTheme.shapes.large,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )
            )

            ExposedDropdownMenu(
                expanded = isCourseExpanded,
                onDismissRequest = {isCourseExpanded = false}
            ) {
                courses.forEach { course ->
                    DropdownMenuItem(
                        text = { Text(course) },
                        onClick = {
                            onCourseSelected(course)
                            isCourseExpanded = false
                        }
                    )
                }
            }
        }

        // Year Selection Chips
        Text(
            text = "Select Year:",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 4.dp)
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            years.forEach { year ->
                FilterChip(
                    selected = selectedYear == year,
                    onClick = { onYearSelected(year) },
                    label = { Text(year) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }
    }
}

// Updated SignupForm with improved course/year selection
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SignupForm(
    onSignupSuccess: () -> Unit,
    onSwitchToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var selectedCourse by remember { mutableStateOf("") }
    var selectedYear by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var acceptTerms by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Email Field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("University Email") },
            placeholder = { Text("student@university.edu") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(Icons.Default.School, contentDescription = "Email")
            },
            singleLine = true,
            shape = MaterialTheme.shapes.large,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
        )

        // Password Field with strength indicator
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Create Password") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = "Password")
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility
                                         else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password"
                                                else "Show password"
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None
                                     else PasswordVisualTransformation(),
                singleLine = true,
                shape = MaterialTheme.shapes.large,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )
            )

            // Password strength indicator
            if (password.isNotEmpty()) {
                val strength = calculatePasswordStrength(password)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(4) { index ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(4.dp)
                                .background(
                                    color = if (index < strength) getStrengthColor(strength)
                                           else Color.Gray.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(2.dp)
                                )
                        )
                    }
                    Text(
                        text = getStrengthText(strength),
                        style = MaterialTheme.typography.labelSmall,
                        color = getStrengthColor(strength)
                    )
                }
            }
        }

        // Confirm Password Field
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(Icons.Default.Verified, contentDescription = "Confirm Password")
            },
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        imageVector = if (confirmPasswordVisible) Icons.Default.Visibility
                                     else Icons.Default.VisibilityOff,
                        contentDescription = if (confirmPasswordVisible) "Hide password"
                                            else "Show password"
                    )
                }
            },
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None
                                 else PasswordVisualTransformation(),
            singleLine = true,
            isError = password.isNotEmpty() && confirmPassword.isNotEmpty() && password != confirmPassword,
            shape = MaterialTheme.shapes.large,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                errorIndicatorColor = MaterialTheme.colorScheme.error
            )
        )

        if (password.isNotEmpty() && confirmPassword.isNotEmpty() && password != confirmPassword) {
            Text(
                text = "Passwords do not match",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        // Course and Year Selection
        CourseYearSelection(
            selectedCourse = selectedCourse,
            selectedYear = selectedYear,
            onCourseSelected = { selectedCourse = it },
            onYearSelected = { selectedYear = it }
        )

        // Terms and Conditions with link
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = acceptTerms,
                onCheckedChange = { acceptTerms = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary
                )
            )
            Text(
                text = "I agree to the ",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 8.dp)
            )
            TextButton(
                onClick = { /* Open terms */ },
                modifier = Modifier.padding(0.dp)
            ) {
                Text(
                    text = "Terms & Conditions",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = " and ",
                style = MaterialTheme.typography.bodySmall
            )
            TextButton(
                onClick = { /* Open privacy policy */ },
                modifier = Modifier.padding(0.dp)
            ) {
                Text(
                    text = "Privacy Policy",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Sign Up Button
        Button(
            onClick = {
                isLoading = true
                // In real app, call signup API here
                // For demo, simulate API call
                onSignupSuccess()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            enabled = email.isNotBlank() &&
                     password.isNotBlank() &&
                     password == confirmPassword &&
                     selectedCourse.isNotBlank() &&
                     selectedYear.isNotBlank() &&
                     acceptTerms &&
                     !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Create Account",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = "Sign Up"
                )
            }
        }

        // Quick Login
        TextButton(
            onClick = onSwitchToLogin,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Already have an account? Sign In")
        }
    }
}

// Helper functions for password strength
private fun calculatePasswordStrength(password: String): Int {
    var strength = 0

    // Check length
    if (password.length >= 8) strength++

    // Check for uppercase
    if (password.any { it.isUpperCase() }) strength++

    // Check for numbers
    if (password.any { it.isDigit() }) strength++

    // Check for special characters
    if (password.any { !it.isLetterOrDigit() }) strength++

    return minOf(strength, 4)
}

private fun getStrengthColor(strength: Int): Color {
    return when (strength) {
        1 -> Color(0xFFF72585) // Weak - Red
        2 -> Color(0xFFFF9E00) // Fair - Orange
        3 -> Color(0xFF4CC9F0) // Good - Blue
        4 -> Color(0xFF06D6A0) // Strong - Green
        else -> Color.Gray
    }
}

private fun getStrengthText(strength: Int): String {
    return when (strength) {
        1 -> "Weak"
        2 -> "Fair"
        3 -> "Good"
        4 -> "Strong"
        else -> ""
    }
}

@Composable
fun SocialLoginButtons() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // Google Sign In
        OutlinedButton(
            onClick = { /* Google sign in */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(Color.White, CircleShape)
                ) {
                    Text(
                        text = "G",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text("Continue with Google")
            }
        }

        // Microsoft Sign In
        OutlinedButton(
            onClick = { /* Microsoft sign in */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "M",
                    color = Color(0xFF00A4EF),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text("Continue with Microsoft")
            }
        }
    }
}

@Composable
fun AuthFooter() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "By continuing, you agree to our",
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.7f)
        )
        Row {
            TextButton(onClick = { /* Terms */ }) {
                Text(
                    text = "Terms of Service",
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                text = " & ",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f)
            )
            TextButton(onClick = { /* Privacy */ }) {
                Text(
                    text = "Privacy Policy",
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // App Info
        Text(
            text = "IIPH v1.0.0",
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.5f)
        )
    }
}
