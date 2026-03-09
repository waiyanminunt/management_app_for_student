package com.supporterunt.backend.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtTokenProvider {

    @Value("\${app.jwt-secret:ThisIsAVerySecretKeyThatWeWillUseForJWTAuthentication1234567890}")
    private lateinit var jwtSecret: String

    @Value("\${app.jwt-expiration-milliseconds:86400000}")
    private var jwtExpirationDate: Long = 0

    fun generateToken(authentication: Authentication): String {
        val username = authentication.name
        val currentDate = Date()
        val expireDate = Date(currentDate.time + jwtExpirationDate)

        return Jwts.builder()
            .subject(username)
            .issuedAt(Date())
            .expiration(expireDate)
            .signWith(key())
            .compact()
    }

    private fun key(): SecretKey {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(Base64.getEncoder().encodeToString(jwtSecret.toByteArray())))
    }

    fun getUsername(token: String): String {
        return Jwts.parser()
            .verifyWith(key())
            .build()
            .parseSignedClaims(token)
            .payload
            .subject
    }

    fun validateToken(token: String): Boolean {
        try {
            Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
            return true
        } catch (ex: Exception) {
            // Log exceptions for malformed, expired, etc.
        }
        return false
    }
}
