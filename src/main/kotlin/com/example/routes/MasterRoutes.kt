package com.example.routes

import com.example.data.models.topic.toDto
import com.example.data.utils.getClaimValueFromJWT
import com.example.services.MongoDBManager
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getMasterDataRoute() {
    get{
        val userId = call.getClaimValueFromJWT("id") ?: ""
        call.respond(MongoDBManager.masterRepository.getAllData(userId).map { it.toDto() })
    }
}