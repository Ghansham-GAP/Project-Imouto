package com.example.iiph.model

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val course: String,
    @SerializedName("current_year")
    val currentYear: Int
)