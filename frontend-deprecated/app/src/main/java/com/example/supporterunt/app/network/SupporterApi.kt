package com.example.supporterunt.app.network

import retrofit2.Response
import retrofit2.http.*

data class LoginRequest(val email: String, val passwordHash: String)
data class JwtAuthResponse(val accessToken: String, val tokenType: String, val role: String)

data class RegisterRequest(
    val name: String,
    val email: String,
    val passwordHash: String,
    val role: String
)

interface SupporterApi {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<JwtAuthResponse>

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<Map<String, String>>

    // Other endpoints can be added here mirroring the backend structure
    //@GET("/api/classes")
    //suspend fun getAllClasses(@Header("Authorization") token: String): Response<List<CourseClass>>
}
