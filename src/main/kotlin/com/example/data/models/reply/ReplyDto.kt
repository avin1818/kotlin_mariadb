package com.example.data.models.reply

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class ReplyDto(
    val id: String? = null,
    val postId: String,
    val text: String,
    val createdBy: String = "",
    val createdByName: String
)

fun ReplyDto.toReplyModel() = Reply(
    postId = ObjectId(this.postId),
    text = this.text,
    userId = ObjectId(this.createdBy),
    createdByName = this.createdByName
)