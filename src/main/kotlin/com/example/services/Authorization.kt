package com.example.services

import com.example.data.models.ManagementRole
import com.example.data.models.Role
import com.example.data.utils.getClaimValueFromJWT
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*

fun Route.roleAuthorization(requiredRoles: List<String>) {
    intercept(ApplicationCallPipeline.Call) {
        val role = call.getClaimValueFromJWT("role") ?: ""
        println("role: $role")

        if (role !in requiredRoles) {
            call.respond(HttpStatusCode.Forbidden, "You are not authorized to access this resource.")
            finish()
        } else {
            proceed()
        }
    }
}

fun Route.authorization(requiredRoles: ArrayList<String>){
    intercept(ApplicationCallPipeline.Call){
        val email = call.getClaimValueFromJWT("email") ?: ""
        println("email: $email")

        val privilegedUser = MongoDBManager.privilegedUserRepository.findByEmail(email)

        privilegedUser?.let {
            println("user roles: ${it.roles}, required roles: $requiredRoles")
            if(it.roles.containsAll(requiredRoles))
                proceed()
            else{
                call.respond(HttpStatusCode.Forbidden, "You are not authorized to access this resource.")
                finish()
            }
        } ?: run {
            call.respond(HttpStatusCode.Forbidden, "User not found")
            finish()
        }
    }
}