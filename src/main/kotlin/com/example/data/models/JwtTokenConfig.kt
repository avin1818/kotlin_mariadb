package com.example.data.models

data class JwtTokenConfig(
    val issuer: String,
    val audience: String,
    val expiresIn: Long,
    val secret: String
)