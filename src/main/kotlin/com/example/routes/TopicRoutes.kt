package com.example.routes

import com.example.data.models.ErrorResponse
import com.example.data.models.Request.MarkAsFavRequest
import com.example.data.models.topic.TopicDto
import com.example.data.models.topic.toDto2
import com.example.data.models.topic.toTopicModel
import com.example.data.utils.getClaimValueFromJWT
import com.example.services.MongoDBManager
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getTopicsRoute(){
    get{
        val userId = call.getClaimValueFromJWT("id") ?: ""
        call.respond(MongoDBManager.topicRepository.getAll(userId).map { it.toDto2() })
    }
}

fun Route.markTopicAsFavRoute(){
    post("/markAsFav"){
        val request = call.receive<MarkAsFavRequest>()
        val userId = call.getClaimValueFromJWT("id") ?: ""

        request.userId = userId

        when(val result = MongoDBManager.userFavRepository.markAsFav(request)){
            is Boolean -> {
                if(request.status)
                    call.respond("Marked as fav")
                else
                    call.respond("Unmarked as fav")
            }
            is String -> {
                call.respond(result)
            }
        }
    }
}

fun Route.addTopicRoute(){
    post {
        val request = call.receive<TopicDto>()
        val topic = request.toTopicModel()

        val topicInserted = MongoDBManager.topicRepository.insert(topic)

        if(topicInserted){
            call.respond("Topic inserted")
        }else
            call.respond(HttpStatusCode.BadRequest, ErrorResponse.BAD_REQUEST_RESPONSE)
    }
}

fun Route.editTopicRoute(){
    put("/{id}"){
        val id = call.parameters["id"].toString()
        val request = call.receive<TopicDto>()
        val topic = request.toTopicModel()

        val topicUpdated = MongoDBManager.topicRepository.updateById(id, topic)

        if(topicUpdated){
            call.respond("Topic updated")
        }else
            call.respond(HttpStatusCode.BadRequest, ErrorResponse.BAD_REQUEST_RESPONSE)
    }
}

fun Route.deleteTopicRoute(){
    delete("/{id}"){
        val id = call.parameters["id"].toString()

        val topicDeleted = MongoDBManager.topicRepository.deleteById(id)

        if(topicDeleted){
            call.respond("Topic deleted")
        }else
            call.respond(HttpStatusCode.BadRequest, ErrorResponse.BAD_REQUEST_RESPONSE)
    }
}