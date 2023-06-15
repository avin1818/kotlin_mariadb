package com.example.services

import com.example.data.models.privilegedUser.PrivilegedUser
import com.example.interfaces.PrivilegedUserRepository
import com.mongodb.client.MongoCollection
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.replaceOne

class MongoPrivilegedUserRepository(private val privilegedUsersCollection: MongoCollection<PrivilegedUser>): MongoRepository<PrivilegedUser>(privilegedUsersCollection),
    PrivilegedUserRepository {
    override fun createPrivilegedUserIfNotExists(user: PrivilegedUser): Any {
        val existingUser = privilegedUsersCollection.findOne(PrivilegedUser::email eq user.email)
        return if(existingUser == null){
            privilegedUsersCollection.insertOne(user)
            user
        }else
            false
    }

    override fun findByEmail(email: String): PrivilegedUser? {
        return try{
            privilegedUsersCollection.findOne(PrivilegedUser::email eq email)
        }catch (e: Exception){
            null
        }
    }

    override fun updateRoles(id: String, roles: ArrayList<String>): Boolean =
        findById(id)
            ?.let{ user ->
                val updateResult = privilegedUsersCollection.replaceOne(user.copy(roles = roles))
                updateResult.modifiedCount == 1L
            } ?: false


}