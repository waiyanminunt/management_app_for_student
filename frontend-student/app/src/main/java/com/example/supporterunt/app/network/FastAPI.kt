package com.example.supporterunt.app.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

// Models
data class LoginRequest(val email: String, val passwordHash: String)
data class JwtAuthResponse(val access_token: String, val token_type: String, val role: String)

data class RegisterRequest(val name: String, val email: String, val password: String, val role: String)
data class RegisterResponse(val name: String, val email: String, val role: String, val id: Int)

// API Interface
interface FastAPIAuthService {
    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): JwtAuthResponse

    @POST("/api/auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse
}

// Network Module
object NetworkModule {
    // 10.0.2.2 points to 127.0.0.1 on the Android Emulator's host machine.
    private const val BASE_URL = "http://10.0.2.2:8000/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: FastAPIAuthService by lazy {
        retrofit.create(FastAPIAuthService::class.java)
    }
}
