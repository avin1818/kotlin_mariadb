package com.example.data.models.Request

import kotlinx.serialization.Serializable

@Serializable
data class OtpRequest(
    val phoneNumber: String
)