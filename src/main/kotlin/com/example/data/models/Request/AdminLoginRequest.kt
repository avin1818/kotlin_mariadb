package com.example.data.models.Request

import kotlinx.serialization.Serializable

@Serializable
data class AdminLoginRequest(
    val email: String,
    val password: String
)