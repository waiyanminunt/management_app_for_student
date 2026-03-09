package com.supporterunt.backend.service

import com.supporterunt.backend.dto.LoginDto
import com.supporterunt.backend.dto.RegisterDto
import com.supporterunt.backend.entity.User
import com.supporterunt.backend.repository.UserRepository
import com.supporterunt.backend.security.JwtTokenProvider
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val authenticationManager: AuthenticationManager,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider
) {

    fun login(loginDto: LoginDto): Pair<String, String> {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginDto.email,
                loginDto.passwordHash
            )
        )

        SecurityContextHolder.getContext().authentication = authentication

        val token = jwtTokenProvider.generateToken(authentication)
        
        // Fetch user from DB to get their role
        val user = userRepository.findByEmail(loginDto.email) 
            ?: throw IllegalArgumentException("User not found")

        return Pair(token, user.role.name) // Assuming Role is an Enum or String
    }

    fun register(registerDto: RegisterDto): String {
        if (userRepository.findByEmail(registerDto.email) != null) {
            throw IllegalArgumentException("Username/Email already exists")
        }

        val user = User(
            name = registerDto.name,
            email = registerDto.email,
            passwordHash = passwordEncoder.encode(registerDto.passwordHash),
            role = registerDto.role
        )

        userRepository.save(user)
        return "User registered successfully!"
    }
}
