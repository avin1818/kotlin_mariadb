package com.example.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ClaimData(
    var userId: String,
    var role: String
)
