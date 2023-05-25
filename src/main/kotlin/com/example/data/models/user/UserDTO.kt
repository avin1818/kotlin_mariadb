package com.example.data.models.user

import com.example.data.models.Role
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val id: String? = null,
    val username: String,
    val phoneNo: String,
    var isApproved: Boolean = false,
    val role: String = Role.OPERATOR.name
)

fun UserDTO.toUserModel() = User(
    username = this.username,
    phoneNo = this.phoneNo,
    isApproved = this.isApproved,
    role = this.role
)