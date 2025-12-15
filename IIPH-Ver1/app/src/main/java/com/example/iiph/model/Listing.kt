package com.example.iiph.model

import com.google.gson.annotations.SerializedName

data class Listing(
    val id: Int,
    @SerializedName("company_name")
    val companyName: String,
    val role: String,
    val type: String, // "internship" or "job"
    val description: String,
    @SerializedName("apply_link")
    val applyLink: String?
)