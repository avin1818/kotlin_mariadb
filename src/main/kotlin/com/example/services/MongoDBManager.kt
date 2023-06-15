package com.example.services

import com.example.data.models.post.Post
import com.example.data.models.privilegedUser.PrivilegedUser
import com.example.data.models.refreshToken.RefreshToken
import com.example.data.models.reply.Reply
import com.example.data.models.thread.Thread
import com.example.data.models.topic.Topic
import com.example.data.models.user.User
import com.example.data.models.user_favorite.UserFavorite
import com.example.interfaces.*
import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.Indexes
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection

object MongoDBManager {

    private val client = KMongo.createClient()
    private val database = client.getDatabase("putz-db")

    init {
        database.getCollection<User>().createIndex(Indexes.ascending("phoneNo"), IndexOptions().unique(true))
        database.getCollection<UserFavorite>().createIndex(Indexes.ascending("userId"), IndexOptions().unique(true))
        database.getCollection<PrivilegedUser>().createIndex(Indexes.ascending("email"), IndexOptions().unique(true))
    }

    val userRepository: UserRepository by lazy {
        MongoUserRepository(database.getCollection<User>())
    }

    val privilegedUserRepository: PrivilegedUserRepository by lazy {
        MongoPrivilegedUserRepository(database.getCollection<PrivilegedUser>())
    }

    val topicRepository: TopicRepository by lazy {
        MongoTopicRepository(database.getCollection<Topic>(), database.getCollection<UserFavorite>())
    }

    val threadRepository: ThreadRepository by lazy {
        MongoThreadRepository(database.getCollection<Thread>())
    }

    val postRepository: PostRepository by lazy {
        MongoPostRepository(database.getCollection<Post>(),
            database.getCollection<UserFavorite>(), database.getCollection<Reply>())
    }

    val masterRepository: MasterRepository by lazy {
        MongoMasterRepository(database.getCollection<Topic>(),
            database.getCollection<Thread>(), database.getCollection<Post>(),
            database.getCollection<UserFavorite>(), database.getCollection<Reply>())
    }

    val userFavRepository: UserFavRepository by lazy {
        MongoUserFavRepository(database.getCollection<UserFavorite>())
    }

    val replyRepository: ReplyRepository by lazy {
        MongoReplyRepository(database.getCollection<Reply>())
    }

    val refreshTokenRepository: RefreshTokenRepository by lazy {
        MongoRefreshTokenRepository(database.getCollection<RefreshToken>())
    }

}