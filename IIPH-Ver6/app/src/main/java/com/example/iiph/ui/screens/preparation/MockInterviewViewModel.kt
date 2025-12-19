package com.example.iiph.ui.screens.preparation

import androidx.lifecycle.ViewModel
import com.example.iiph.data.MockInterviewQuestion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MockInterviewViewModel : ViewModel() {

    private val _questions = MutableStateFlow<List<MockInterviewQuestion>>(emptyList())

    private val _currentQuestion = MutableStateFlow<MockInterviewQuestion?>(null)
    val currentQuestion = _currentQuestion.asStateFlow()

    private val _feedback = MutableStateFlow("")
    val feedback = _feedback.asStateFlow()

    private val _interviewCategory = MutableStateFlow("")
    val interviewCategory = _interviewCategory.asStateFlow()

    private val _questionIndex = MutableStateFlow(0)
    val questionIndex = _questionIndex.asStateFlow()

    val totalQuestions: Int
        get() = _questions.value.size

    private val technicalQuestions = listOf(
        MockInterviewQuestion("1", "Technical", "Explain the difference between `val` and `var` in Kotlin."),
        MockInterviewQuestion("2", "Technical", "What are the four main principles of Object-Oriented Programming?"),
        MockInterviewQuestion("3", "Technical", "Describe the Android Activity lifecycle."),
        MockInterviewQuestion("4", "Technical", "What is a REST API?"),
        MockInterviewQuestion("5", "Technical", "What is the purpose of a version control system like Git?")
    )

    private val hrQuestions = listOf(
        MockInterviewQuestion("1", "HR", "Tell me about yourself."),
        MockInterviewQuestion("2", "HR", "What are your biggest strengths?"),
        MockInterviewQuestion("3", "HR", "What are your biggest weaknesses?"),
        MockInterviewQuestion("4", "HR", "Why do you want to work for this company?"),
        MockInterviewQuestion("5", "HR", "Describe a challenging situation you\'ve faced and how you handled it.")
    )

    fun startInterview(category: String) {
        _interviewCategory.value = category
        _questions.value = if (category == "Technical") technicalQuestions else hrQuestions
        _questionIndex.value = 0
        _currentQuestion.value = _questions.value.firstOrNull()
        _feedback.value = ""
    }

    fun submitAnswer(answer: String) {
        _feedback.value = "This is a great start! A more detailed answer could also include..."
    }

    fun nextQuestion() {
        if (_questionIndex.value < _questions.value.size - 1) {
            _questionIndex.value++
            _currentQuestion.value = _questions.value[_questionIndex.value]
            _feedback.value = ""
        } else {
            _currentQuestion.value = null // End of interview
        }
    }
}