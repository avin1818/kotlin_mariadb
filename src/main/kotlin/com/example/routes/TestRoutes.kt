package com.example.routes

import com.example.data.models.ClaimData
import com.example.data.utils.getClaimValueFromJWT
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.testRoute(){
    get("test"){
        call.respond("Success")
    }
}

fun Route.testJWTRoute(){
    get("/fetch-token-data"){
        val userId = call.getClaimValueFromJWT("id") ?: ""
        val role = call.getClaimValueFromJWT("role") ?: ""
        val claimData = ClaimData(userId, role)
        call.respond(claimData)
    }
}