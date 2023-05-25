package com.example.services

import com.example.data.models.Request.MarkAsFavRequest
import com.example.data.models.user_favorite.UserFavorite
import com.example.interfaces.UserFavRepository
import com.mongodb.client.MongoCollection
import org.bson.types.ObjectId
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.replaceOne

class MongoUserFavRepository(private val userFavsCollection: MongoCollection<UserFavorite>): MongoRepository<UserFavorite>(userFavsCollection), UserFavRepository {

    override fun markAsFav(request: MarkAsFavRequest): Any =
        userFavsCollection.findOne(UserFavorite::userId eq ObjectId(request.userId.toString()))
            ?.let{ userFav ->
                var msg = ""
                if(request.status){
                    if(!userFav.favTopics.contains(ObjectId(request.topicId)))
                        userFav.favTopics += ObjectId(request.topicId)
                    else
                        msg = "Already marked as fav"
                } else{
                    if(userFav.favTopics.contains(ObjectId(request.topicId)))
                        userFav.favTopics -= ObjectId(request.topicId)
                    else
                        msg = "Already unmarked as fav"
                }
                msg.ifEmpty {
                    val updateResult = userFavsCollection.replaceOne(
                        userFav.copy(
                            userId = ObjectId(request.userId),
                            favTopics = userFav.favTopics
                        )
                    )
                    updateResult.modifiedCount == 1L
                }
            } ?: false

}