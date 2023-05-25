package com.example.data.models.user_favorite

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import org.litote.kmongo.Id

data class UserFavorite(
    @BsonId
    val id: Id<UserFavorite>? = null,
    val userId: ObjectId,
    var favTopics: List<ObjectId>
)
