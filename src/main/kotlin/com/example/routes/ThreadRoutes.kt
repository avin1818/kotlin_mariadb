package com.example.routes

import com.example.data.models.ErrorResponse
import com.example.data.models.thread.ThreadDto
import com.example.data.models.thread.toDto2
import com.example.data.models.thread.toThreadModel
import com.example.services.MongoDBManager
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getThreadsForTopicsRoute() {
    get("/{topicId}"){
        val topicId = call.parameters["topicId"].toString()
        call.respond(MongoDBManager.threadRepository.findByTopicId(topicId).map { it.toDto2() })
    }
}

fun Route.addThreadRoute(){
    post {
        val request = call.receive<ThreadDto>()
        val thread = request.toThreadModel()

        val threadInserted = MongoDBManager.threadRepository.insert(thread)

        if(threadInserted){
            call.respond("Thread inserted")
        }else
            call.respond(HttpStatusCode.BadRequest, ErrorResponse.BAD_REQUEST_RESPONSE)
    }
}

fun Route.editThreadRoute(){
    put("/{id}"){
        val id = call.parameters["id"].toString()
        val request = call.receive<ThreadDto>()
        val thread = request.toThreadModel()

        val threadUpdated = MongoDBManager.threadRepository.updateById(id, thread)

        if(threadUpdated){
            call.respond("Thread updated")
        }else
            call.respond(HttpStatusCode.BadRequest, ErrorResponse.BAD_REQUEST_RESPONSE)
    }
}

fun Route.deleteThreadRoute(){
    delete("/{id}"){
        val id = call.parameters["id"].toString()

        val threadDeleted = MongoDBManager.threadRepository.deleteById(id)

        if(threadDeleted){
            call.respond("Thread deleted")
        }else
            call.respond(HttpStatusCode.BadRequest, ErrorResponse.BAD_REQUEST_RESPONSE)
    }
}