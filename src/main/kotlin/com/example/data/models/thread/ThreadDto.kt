package com.example.data.models.thread

import com.example.data.models.post.PostDto
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class ThreadDto(
    val id: String? = null,
    val topicId: String? = null,
    val name: String,
    val posts: List<PostDto> = emptyList()
)

fun ThreadDto.toThreadModel() = Thread(
    name = this.name,
    topicId = ObjectId(this.topicId)
)