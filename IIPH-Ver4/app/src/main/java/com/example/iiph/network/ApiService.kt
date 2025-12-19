package com.example.iiph.network

import com.example.iiph.model.Listing
import com.example.iiph.model.Message
import com.example.iiph.model.User
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("login.php")
    suspend fun login(
        @Field("email") email: String,
        @Field("pass") pass: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("register.php")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("pass") pass: String,
        @Field("course") course: String,
        @Field("year") year: Int
    ): Response<RegisterResponse>

    @GET("get_listings.php")
    suspend fun getListings(): Response<List<Listing>>

    @GET("get_messages.php")
    suspend fun getMessages(): Response<List<Message>>

    @FormUrlEncoded
    @POST("send_message.php")
    suspend fun sendMessage(
        @Field("sender_id") senderId: Int,
        @Field("receiver_id") receiverId: Int,
        @Field("message_text") messageText: String
    ): Response<SendMessageResponse>
}

data class LoginResponse(
    val status: String,
    val message: String?,
    val user: User?
)

data class RegisterResponse(
    val status: String,
    val message: String?
)

data class SendMessageResponse(
    val status: String,
    val message: String?
)