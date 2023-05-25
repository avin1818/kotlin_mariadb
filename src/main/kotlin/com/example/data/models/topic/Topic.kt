package com.example.data.models.topic

import com.example.data.models.thread.Thread
import com.example.data.models.thread.toDto
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id

data class Topic(
    @BsonId
    val id: Id<Topic>? = null,
    val name: String,
    var isFav: Boolean = false,
    var threads: List<Thread> = emptyList()
)

fun Topic.toDto() = TopicDto(
    id = this.id.toString(),
    name = this.name,
    threads = this.threads.map { it.toDto() },
    isFav = this.isFav
)

fun Topic.toDto2() = TopicDto2(
    id = this.id.toString(),
    name = this.name,
    isFav = this.isFav
)