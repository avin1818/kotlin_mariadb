package com.example.services

import com.example.data.models.user.User
import com.example.interfaces.UserRepository
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.Indexes
import org.bson.types.ObjectId
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.findOneById
import org.litote.kmongo.replaceOne

class MongoUserRepository(private val usersCollection: MongoCollection<User>): MongoRepository<User>(usersCollection), UserRepository {

    override fun createUserIfNotExists(user: User): Any {
        val existingUser = usersCollection.findOne(User::phoneNo eq user.phoneNo)
        return if(existingUser == null){
            usersCollection.insertOne(user)
            user
        }else
            false
    }

    override fun findByPhoneNo(phoneNo: String): User? {
        return try{
            usersCollection.findOne(User::phoneNo eq phoneNo)
        }catch (e: Exception){
            null
        }
    }

    override fun approveUser(userId: String): Any {
        return usersCollection.findOneById(ObjectId(userId))?.let { user ->
            if(!user.isApproved){
                val updateResult = usersCollection.replaceOne(
                    user.copy(
                        isApproved = true
                    )
                )
                updateResult.modifiedCount == 1L
            }else{
                "User already approved"
            }
        } ?: "User not found"
    }

}