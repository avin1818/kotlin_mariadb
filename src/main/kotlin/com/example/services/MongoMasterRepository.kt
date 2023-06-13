package com.example.services

import com.example.data.models.post.Post
import com.example.data.models.reply.Reply
import com.example.data.models.thread.Thread
import com.example.data.models.topic.Topic
import com.example.data.models.user_favorite.UserFavorite
import com.example.interfaces.MasterRepository
import com.mongodb.client.MongoCollection
import org.bson.types.ObjectId
import org.litote.kmongo.eq
import org.litote.kmongo.findOne

class MongoMasterRepository(private val topicsCollection: MongoCollection<Topic>,
                            private val threadsCollection: MongoCollection<Thread>,
                            private val postsCollection: MongoCollection<Post>,
                            private val userFavsCollection: MongoCollection<UserFavorite>,
                            private val replyCollection: MongoCollection<Reply>)
    : MongoRepository<Topic>(topicsCollection), MasterRepository {

    override fun getAllData(userId: String): List<Topic> {
        val topics = topicsCollection.find().toList()

        for(topic in topics){
            val topicId = ObjectId(topic.id.toString())
            val threads = threadsCollection.find(Thread::topicId eq topicId).toList()

            val userFav = userFavsCollection.findOne(UserFavorite::userId eq ObjectId(userId))

            val isFav = userFav?.favTopics?.contains(ObjectId(topic.id.toString())) ?: false
            topic.isFav = isFav


            for(thread in threads){
                val threadId = ObjectId(thread.id.toString())
                val posts = postsCollection.find(Post::threadId eq threadId).toList()


                for(post in posts){
                    val replies = replyCollection.find(Reply::postId eq ObjectId(post.id.toString())).toList()
                    post.replies = replies.take(3)
                    println("postId: ${post.id}")
                }

                thread.posts = posts

            }

            topic.threads = threads
        }

        return topics

    }

}