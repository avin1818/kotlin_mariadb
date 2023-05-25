package com.example.data.models.topic

import com.example.data.models.thread.ThreadDto
import kotlinx.serialization.Serializable

@Serializable
data class TopicDto(
    val id: String? = null,
    val name: String,
    var isFav: Boolean = false,
    val threads: List<ThreadDto> = listOf()
)

fun TopicDto.toTopicModel() = Topic(
    name = this.name,
    isFav = this.isFav
)