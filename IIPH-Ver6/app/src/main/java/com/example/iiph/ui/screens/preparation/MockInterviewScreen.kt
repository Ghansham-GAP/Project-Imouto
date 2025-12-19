package com.example.iiph.ui.screens.preparation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.iiph.data.MockInterviewQuestion

@Composable
fun MockInterviewScreen(
    mockInterviewViewModel: MockInterviewViewModel = viewModel()
) {
    val currentQuestion by mockInterviewViewModel.currentQuestion.collectAsState()
    val interviewCategory by mockInterviewViewModel.interviewCategory.collectAsState()

    if (currentQuestion == null) {
        StartInterviewScreen(mockInterviewViewModel)
    } else {
        InterviewScreen(mockInterviewViewModel, interviewCategory)
    }
}

@Composable
fun StartInterviewScreen(mockInterviewViewModel: MockInterviewViewModel) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Start a Mock Interview", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = { mockInterviewViewModel.startInterview("Technical") }, modifier = Modifier.fillMaxWidth()) {
            Text("Start Technical Interview")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { mockInterviewViewModel.startInterview("HR") }, modifier = Modifier.fillMaxWidth()) {
            Text("Start HR Interview")
        }
    }
}

@Composable
fun InterviewScreen(
    viewModel: MockInterviewViewModel,
    category: String
) {
    val question by viewModel.currentQuestion.collectAsState()
    val feedback by viewModel.feedback.collectAsState()
    val questionIndex by viewModel.questionIndex.collectAsState()
    val totalQuestions = viewModel.totalQuestions

    var answer by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text(category, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Question ${questionIndex + 1} of $totalQuestions", style = MaterialTheme.typography.bodyMedium)
            Divider(modifier = Modifier.padding(vertical = 16.dp))
        }

        // Question
        Text(text = question?.question ?: "", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(bottom = 16.dp))

        // Answer Field
        OutlinedTextField(
            value = answer,
            onValueChange = { answer = it },
            label = { Text("Your Answer") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Feedback & Buttons
        if (feedback.isNotEmpty()) {
            Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)) {
                Text(text = feedback, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(16.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.nextQuestion() }, modifier = Modifier.fillMaxWidth()) {
                Text("Next Question")
            }
        } else {
            Button(onClick = { viewModel.submitAnswer(answer) }, modifier = Modifier.fillMaxWidth(), enabled = answer.isNotBlank()) {
                Text("Submit Answer")
            }
        }
    }
}
