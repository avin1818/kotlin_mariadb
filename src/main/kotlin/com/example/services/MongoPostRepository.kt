package com.example.services

import com.example.data.models.Request.MarkAsFavRequest
import com.example.data.models.post.Post
import com.example.data.models.reply.Reply
import com.example.data.models.topic.Topic
import com.example.data.models.user_favorite.UserFavorite
import com.example.interfaces.PostRepository
import com.mongodb.client.MongoCollection
import org.bson.types.ObjectId
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.findOneById
import org.litote.kmongo.replaceOne

class MongoPostRepository(private val postsCollection: MongoCollection<Post>,
                          private val userFavsCollection: MongoCollection<UserFavorite>,
                          private val replyCollection: MongoCollection<Reply>): MongoRepository<Post>(postsCollection), PostRepository {

    override fun findByThreadId(id: String, userId: String): List<Post> {
        return try{
            val posts = postsCollection.find(Post::threadId eq ObjectId(id)).toList()
            for(post in posts){
                val replies = replyCollection.find(Reply::postId eq ObjectId(post.id.toString())).toList()
                post.replies = replies.take(3)
                println("postId: ${post.id}")
            }
            posts
        }catch (e: Exception){
            emptyList<Post>()
        }
    }

    override fun approvePost(postId: String): Any {
        return postsCollection.findOneById(ObjectId(postId))?.let { post ->
            if(!post.isApproved){
                val updateResult = postsCollection.replaceOne(
                    post.copy(
                        isApproved = true
                    )
                )
                updateResult.modifiedCount == 1L
            }else{
                "Post already approved"
            }
        } ?: "Post not found"
    }

    override fun updateById(id: String, request: Post): Boolean =
        findById(id)
            ?.let{ post ->
                val updateResult = postsCollection.replaceOne(post.copy(text = request.text,
                    threadId = request.threadId))
                updateResult.modifiedCount == 1L
            } ?: false

}