package com.example.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.data.models.JwtTokenConfig
import com.example.data.models.user.UserDTO
import java.util.*
class JwtAuthService {

    fun generateToken(user: UserDTO, jwtTokenConfig: JwtTokenConfig): String = JWT.create()
        .withIssuer(jwtTokenConfig.issuer)
        .withClaim("id", user.id)
        .withClaim("role", user.role)
        .withAudience(jwtTokenConfig.audience)
        .withExpiresAt(getExpiration(jwtTokenConfig.expiresIn))
        .sign(Algorithm.HMAC512(jwtTokenConfig.secret))

    fun generateRefreshToken(user: UserDTO, jwtTokenConfig: JwtTokenConfig): String = JWT.create()
        .withIssuer(jwtTokenConfig.issuer)
        .withClaim("id", user.id)
        .withClaim("role", user.role)
        .withAudience(jwtTokenConfig.audience)
        .sign(Algorithm.HMAC512(jwtTokenConfig.secret))

    fun verifyToken(token: String, jwtTokenConfig: JwtTokenConfig): Boolean{
        return try {
            JWT.require(Algorithm.HMAC512(jwtTokenConfig.secret))
                .withIssuer(jwtTokenConfig.issuer)
                .build()
                .verify(token)
            true
        }catch (e: Exception){
            false
        }

    }

    private fun getExpiration(expiresInMs: Long) = Date(System.currentTimeMillis() + expiresInMs)

}

