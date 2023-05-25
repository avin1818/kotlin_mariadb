package com.example.interfaces

import org.bson.types.ObjectId


interface Repository<T> {
    fun insert(item: T): Boolean
    fun insertAll(items: List<T>)
    fun findAll(): List<T>
    fun findById(id: String): T?
    fun deleteById(id: String): Boolean
}