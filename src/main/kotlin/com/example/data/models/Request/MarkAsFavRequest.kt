package com.example.data.models.Request

import kotlinx.serialization.Serializable

@Serializable
data class MarkAsFavRequest(
    val topicId: String,
    var userId: String? = null,
    val status: Boolean
)