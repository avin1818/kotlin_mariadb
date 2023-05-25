package com.example.data.models.topic

import com.example.data.models.thread.ThreadDto
import kotlinx.serialization.Serializable

@Serializable
data class TopicDto2(
    val id: String? = null,
    val name: String,
    val isFav: Boolean = false
)

fun TopicDto2.toTopicModel() = Topic(
    name = this.name,
    isFav = this.isFav
)