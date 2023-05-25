package com.example.data.models.Response

import kotlinx.serialization.Serializable

@Serializable
data class VerifyOtpResponse(
    val username: String,
    val phoneNo: String,
    val accessToken: String,
    val refreshToken: String,
    val message: String
)