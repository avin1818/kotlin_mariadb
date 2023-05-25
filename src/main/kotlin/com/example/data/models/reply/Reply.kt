package com.example.data.models.reply

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import org.litote.kmongo.Id

data class Reply(
    @BsonId
    val id: Id<Reply>? = null,
    val postId: ObjectId? = null,
    var userId: ObjectId? = null,
    val text: String,
    var createdByName: String
)

fun Reply.toDto() = ReplyDto(
    id = this.id.toString(),
    postId = this.postId.toString(),
    text = this.text,
    createdBy = this.userId.toString(),
    createdByName = this.createdByName
)