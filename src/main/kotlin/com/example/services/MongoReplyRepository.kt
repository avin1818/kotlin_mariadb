package com.example.services

import com.example.data.models.reply.Reply
import com.example.interfaces.ReplyRepository
import com.mongodb.client.MongoCollection
import org.bson.types.ObjectId
import org.litote.kmongo.eq

class MongoReplyRepository(private val replyCollection: MongoCollection<Reply>)
    : MongoRepository<Reply>(replyCollection), ReplyRepository {

        override fun findByPostId(id: String): List<Reply> {
            return try{
                replyCollection.find(Reply::postId eq ObjectId(id)).toList()
            }catch (e: Exception){
                emptyList<Reply>()
            }
        }

}