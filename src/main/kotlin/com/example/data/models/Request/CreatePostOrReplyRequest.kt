package com.example.data.models.Request

import com.example.data.models.post.Post
import com.example.data.models.reply.Reply
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class CreatePostOrReplyRequest(
    val threadId: String? = null,
    val postId: String? = null,
    val text: String,
    var createdByName: String = "",
    var createdBy: String = ""
)

fun CreatePostOrReplyRequest.toPostModel() = Post(
    threadId = ObjectId(threadId),
    text = text,
    createdByName = createdByName,
    userId = ObjectId(createdBy)
)

fun CreatePostOrReplyRequest.toReplyModel() = Reply(
    text = text,
    postId = ObjectId(postId),
    createdByName = createdByName,
    userId = ObjectId(createdBy)
)