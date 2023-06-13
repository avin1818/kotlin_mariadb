package com.example.routes

import com.example.data.models.ErrorResponse
import com.example.data.models.Response.LogoutResponse
import com.example.data.utils.getClaimValueFromJWT
import com.example.services.MongoDBManager
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.logoutRoute() {
    get{
        val userId = call.getClaimValueFromJWT("id") ?: ""
        val deleted = MongoDBManager.refreshTokenRepository.deleteRefreshTokenForId(userId)
        if(deleted){
            call.respond(
                LogoutResponse("Logout successful")
            )
        }else
            call.respond(HttpStatusCode.InternalServerError, ErrorResponse.INTERNAL_SERVER_ERROR_RESPONSE)
    }
}