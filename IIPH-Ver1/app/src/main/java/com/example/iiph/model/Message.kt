package com.example.iiph.model

import com.google.gson.annotations.SerializedName

data class Message(
    val id: Int,
    @SerializedName("sender_id")
    val senderId: Int,
    @SerializedName("receiver_id")
    val receiverId: Int,
    @SerializedName("message_text")
    val messageText: String,
    val timestamp: String,
    @SerializedName("sender_name")
    val senderName: String
)