package com.example.interfaces

import com.example.data.models.privilegedUser.PrivilegedUser

interface PrivilegedUserRepository: Repository<PrivilegedUser> {
    fun createPrivilegedUserIfNotExists(user: PrivilegedUser): Any
    fun findByEmail(email: String): PrivilegedUser?
    fun updateRoles(id: String, roles: ArrayList<String>): Boolean
}