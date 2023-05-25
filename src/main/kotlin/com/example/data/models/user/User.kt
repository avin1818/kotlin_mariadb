package com.example.data.models.user

import com.example.data.models.Role
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id

data class User(
    @BsonId
    val id: Id<User>? = null,
    val username: String,
    var phoneNo: String,
    var isApproved: Boolean = false,
    val role: String = Role.OPERATOR.name
)

fun User.toDto() = UserDTO(
    id = this.id.toString(),
    username = this.username,
    phoneNo = this.phoneNo,
    isApproved = this.isApproved,
    role = this.role
)