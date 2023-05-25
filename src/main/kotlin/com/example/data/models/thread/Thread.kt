package com.example.data.models.thread

import com.example.data.models.post.Post
import com.example.data.models.post.toDto
import com.example.data.models.topic.Topic
import com.example.data.models.topic.TopicDto
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import org.litote.kmongo.Id

data class Thread(
    @BsonId
    val id: Id<Thread>? = null,
    val topicId: ObjectId? = null,
    val name: String,
    var posts: List<Post> = emptyList()
)

fun Thread.toDto() = ThreadDto(
    id = this.id.toString(),
    topicId = this.topicId.toString(),
    name = this.name,
    posts = this.posts.map { it.toDto() }
)

fun Thread.toDto2() = ThreadDto2(
    id = this.id.toString(),
    topicId = this.topicId.toString(),
    name = this.name
)


