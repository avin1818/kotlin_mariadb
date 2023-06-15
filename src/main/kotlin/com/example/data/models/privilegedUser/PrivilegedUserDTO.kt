package com.example.data.models.privilegedUser

import com.example.data.models.ManagementRole
import kotlinx.serialization.Serializable

@Serializable
data class PrivilegedUserDTO(
    val id: String? = null,
    val email: String,
    val password: String,
    val roles: ArrayList<String> = arrayListOf(ManagementRole.CONTENT_MANAGER.name)
)

fun PrivilegedUserDTO.toPrivilegedUserModel() = PrivilegedUser(
    email = this.email,
    password = this.password,
    roles = this.roles
)