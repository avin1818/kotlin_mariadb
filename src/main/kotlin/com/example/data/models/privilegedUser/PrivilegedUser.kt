package com.example.data.models.privilegedUser

import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id

data class PrivilegedUser(
    @BsonId
    val id: Id<PrivilegedUser>? = null,
    val email: String,
    val password: String,
    val roles: ArrayList<String>
)

fun PrivilegedUser.toDTO() = PrivilegedUserDTO(
    id = this.id.toString(),
    email = this.email,
    password = this.password,
    roles = this.roles
)

