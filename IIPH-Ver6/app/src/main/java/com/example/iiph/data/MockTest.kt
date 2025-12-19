package com.example.iiph.data

data class MockTest(
    val id: String = "",
    val title: String = "",
    val duration: Int = 0, // in minutes
    val questions: List<String> = emptyList()
)