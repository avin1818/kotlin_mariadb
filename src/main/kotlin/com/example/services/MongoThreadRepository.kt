package com.example.services

import com.example.data.models.thread.Thread
import com.example.interfaces.ThreadRepository
import com.mongodb.client.MongoCollection
import org.bson.types.ObjectId
import org.litote.kmongo.eq
import org.litote.kmongo.replaceOne

class MongoThreadRepository(private val threadsCollection: MongoCollection<Thread>): MongoRepository<Thread>(threadsCollection), ThreadRepository {

    override fun findByTopicId(id: String): List<Thread> {
        return try{
            threadsCollection.find(Thread::topicId eq ObjectId(id)).toList()
        }catch (e: Exception){
            emptyList<Thread>()
        }
    }

    override fun updateById(id: String, request: Thread): Boolean =
        findById(id)
            ?.let{ thread ->
                val updateResult = threadsCollection.replaceOne(thread.copy(name = request.name, topicId = request.topicId))
                updateResult.modifiedCount == 1L
            } ?: false

}