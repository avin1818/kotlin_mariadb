package com.example.services

import com.example.data.models.topic.Topic
import com.example.data.models.user_favorite.UserFavorite
import com.example.interfaces.TopicRepository
import com.mongodb.client.MongoCollection
import org.bson.types.ObjectId
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.replaceOne

class MongoTopicRepository(
    private val topicsCollection: MongoCollection<Topic>,
    private val userFavsCollection: MongoCollection<UserFavorite>
)
    : MongoRepository<Topic>(topicsCollection), TopicRepository {
    override fun getAll(userId: String): List<Topic> {
        val topics = topicsCollection.find().toList()

        val userFav = userFavsCollection.findOne(UserFavorite::userId eq ObjectId(userId))

        for(topic in topics){
            val isFav = userFav?.favTopics?.contains(ObjectId(topic.id.toString())) ?: false
            topic.isFav = isFav
        }

        return topics
    }

    override fun updateById(id: String, request: Topic): Boolean =
        findById(id)
            ?.let{ topic ->
                val updateResult = topicsCollection.replaceOne(topic.copy(name = request.name))
                updateResult.modifiedCount == 1L
            } ?: false


}