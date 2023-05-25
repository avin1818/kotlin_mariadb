package com.example.data.models.post

import com.example.data.models.reply.Reply
import com.example.data.models.reply.ReplyDto
import com.example.data.models.reply.toReplyModel
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class PostDto(
    val id: String? = null,
    val threadId: String,
    val text: String,
    val createdBy: String = "",
    val createdByName: String = "",
    val isApproved: Boolean = false,
    var replies: List<ReplyDto> = emptyList()
)

fun PostDto.toPostModel() = Post(
    threadId = ObjectId(this.threadId),
    text = this.text,
    userId = ObjectId(this.createdBy),
    isApproved = this.isApproved,
    createdByName = this.createdByName
)