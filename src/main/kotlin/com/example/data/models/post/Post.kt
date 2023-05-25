package com.example.data.models.post

import com.example.data.models.reply.Reply
import com.example.data.models.reply.toDto
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import org.litote.kmongo.Id

data class Post(
    @BsonId
    val id: Id<Post>? = null,
    val threadId: ObjectId? = null,
    var userId: ObjectId? = null,
    val text: String,
    val isApproved: Boolean = false,
    var createdByName: String,
    var replies: List<Reply> = emptyList()
)

fun Post.toDto() = PostDto(
    id = this.id.toString(),
    threadId = this.threadId.toString(),
    text = this.text,
    createdBy = this.userId.toString(),
    createdByName = this.createdByName,
    isApproved = this.isApproved,
    replies = this.replies.map { it.toDto() }
)