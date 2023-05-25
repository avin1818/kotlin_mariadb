package com.example.services

import com.example.data.utils.getClaimValueFromJWT
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

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