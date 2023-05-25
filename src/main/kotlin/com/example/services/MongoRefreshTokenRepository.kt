package com.example.services

import com.example.data.models.refreshToken.RefreshToken
import com.example.interfaces.RefreshTokenRepository
import com.mongodb.client.MongoCollection
import org.bson.types.ObjectId
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.replaceOne

class MongoRefreshTokenRepository(
    private val refreshTokenCollection: MongoCollection<RefreshToken>
): MongoRepository<RefreshToken>(refreshTokenCollection), RefreshTokenRepository {
    override fun addRefreshToken(token: RefreshToken): Boolean {
        val existingUser = refreshTokenCollection.findOne(
            RefreshToken::userId eq ObjectId(token.userId.toString()))
        return if(existingUser == null){
            refreshTokenCollection.insertOne(token).wasAcknowledged()
        }else
            refreshTokenCollection.replaceOne(existingUser.copy(token = token.token, userId = token.userId)).wasAcknowledged()
    }

    override fun findByRefreshToken(token: String): RefreshToken? =
        refreshTokenCollection.findOne(RefreshToken::token eq token)


    override fun findByUserId(userId: String): RefreshToken? =
        refreshTokenCollection.findOne(RefreshToken::userId eq ObjectId(userId))

    override fun deleteRefreshTokenForId(userId: String): Boolean {
        return findByUserId(userId)?.let { refreshToken ->
            refreshTokenCollection.replaceOne(refreshToken.copy(userId = ObjectId(userId), token = "")).wasAcknowledged()
        } ?: false
    }

}