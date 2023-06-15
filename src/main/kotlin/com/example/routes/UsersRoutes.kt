package com.example.routes

import com.example.data.models.user.toDto
import com.example.services.MongoDBManager
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getUsersRoute() {
    get {
        val page = call.parameters["page"]?.toIntOrNull() ?: 1
        val pageLimit = call.parameters["limit"]?.toIntOrNull() ?: 10

        val startIndex = (page - 1) * pageLimit
        val endIndex = startIndex + pageLimit

        val users = MongoDBManager.userRepository.findAll().map { it.toDto() }
        val pagedUsers = users.subList(startIndex.coerceAtLeast(0), endIndex.coerceAtMost(users.size))

        call.respond(pagedUsers)
    }
}

fun Route.approveUserRoute(){
    get("/approve-user/{userId}"){
        val userId = call.parameters["userId"].toString()

        when(val userApproved = MongoDBManager.userRepository.approveUser(userId)){
            is Boolean -> call.respond("User approved")
            is String -> call.respond(HttpStatusCode.BadRequest, userApproved)
        }
    }
}