package com.supporterunt.backend.controller

import com.supporterunt.backend.dto.JwtAuthResponse
import com.supporterunt.backend.dto.LoginDto
import com.supporterunt.backend.dto.RegisterDto
import com.supporterunt.backend.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.supporterunt.backend.dto.RegisterResponseDto

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/login")
    fun login(@RequestBody loginDto: LoginDto): ResponseEntity<JwtAuthResponse> {
        val (token, role) = authService.login(loginDto)
        return ResponseEntity.ok(JwtAuthResponse(accessToken = token, role = role))
    }


    @PostMapping("/register")
    fun register(@RequestBody registerDto: RegisterDto): ResponseEntity<RegisterResponseDto> {
        return try {
            val response = authService.register(registerDto)
            ResponseEntity(RegisterResponseDto(response), HttpStatus.CREATED)
        } catch (e: IllegalArgumentException) {
            ResponseEntity(RegisterResponseDto(e.message ?: "Error"), HttpStatus.BAD_REQUEST)
        }
    }
}
