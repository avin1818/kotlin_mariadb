package com.example.data.models.Request

import kotlinx.serialization.Serializable

@Serializable
data class VerifyOtpRequest(
    val otp: Int,
    val phoneNumber: String,
    val username: String
)