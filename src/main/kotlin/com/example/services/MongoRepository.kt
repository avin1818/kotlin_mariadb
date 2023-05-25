package com.example.services

import com.example.data.models.user.User
import com.example.interfaces.Repository
import com.mongodb.client.MongoCollection
import org.bson.types.ObjectId
import org.litote.kmongo.*
import org.litote.kmongo.id.toId

open class MongoRepository<T>(private val collection: MongoCollection<T>): Repository<T> {

    override fun insert(item: T): Boolean{
        val insertedItem = collection.insertOne(item)
        return insertedItem.wasAcknowledged()
    }

    override fun insertAll(items: List<T>) {
        collection.insertMany(items)
    }

    override fun findById(id: String): T? = collection.findOneById(ObjectId(id))

    override fun findAll() = collection.find().toList()

    override fun deleteById(id: String): Boolean {
        return try{
            val deleteResult = collection.deleteOneById(ObjectId(id))
            deleteResult.deletedCount == 1L
        }catch (e: Exception){
            false
        }
    }

}