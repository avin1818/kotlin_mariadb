package com.example.interfaces

import com.example.data.models.user.User

interface UserRepository: Repository<User> {
    fun createUserIfNotExists(user: User): Any
    fun findByPhoneNo(phoneNo: String): User?
    fun approveUser(userId: String): Any
}