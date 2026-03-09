package com.supporterunt.backend.dto

import com.supporterunt.backend.entity.Role

data class LoginDto(
    val email: String,
    val passwordHash: String // We will treat this as the password field for now
)

data class RegisterDto(
    val name: String,
    val email: String,
    val passwordHash: String,
    val role: Role
)

data class JwtAuthResponse(
    val accessToken: String,
    val tokenType: String = "Bearer",
    val role: String
)
