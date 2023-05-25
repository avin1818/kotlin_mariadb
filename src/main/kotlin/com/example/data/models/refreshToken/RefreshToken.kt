package com.example.data.models.refreshToken

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import org.litote.kmongo.Id

data class RefreshToken(
    @BsonId
    val id: Id<RefreshToken>? = null,
    val token: String,
    val userId: ObjectId,
    val isDisabled: Boolean = false
)
