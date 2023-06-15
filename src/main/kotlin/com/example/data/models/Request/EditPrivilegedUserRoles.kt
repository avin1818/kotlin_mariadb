package com.example.data.models.Request

import kotlinx.serialization.Serializable

@Serializable
data class EditPrivilegedUserRolesRequest(
    val email: String,
    val roles: ArrayList<String>
)
