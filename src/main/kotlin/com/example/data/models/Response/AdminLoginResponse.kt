package com.example.data.models.Response

import kotlinx.serialization.Serializable

@Serializable
data class AdminLoginResponse(
    val email: String,
    val accessToken: String,
    val refreshToken: String,
    val message: String
)
