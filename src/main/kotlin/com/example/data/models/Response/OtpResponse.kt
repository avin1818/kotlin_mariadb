package com.example.data.models.Response

import kotlinx.serialization.Serializable

@Serializable
data class OtpResponse(
    val otp: Int,
    val message: String
)