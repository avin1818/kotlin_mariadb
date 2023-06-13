package com.example.routes

import com.example.data.models.reply.toDto
import com.example.services.MongoDBManager
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getRepliesForTopicRoute(){
    get("/{postId}") {

        val postId = call.parameters["postId"].toString()

        val page = call.parameters["page"]?.toIntOrNull() ?: 1
        val pageLimit = call.parameters["limit"]?.toIntOrNull() ?: 10

        val startIndex = (page - 1) * pageLimit
        val endIndex = startIndex + pageLimit

        val replies = MongoDBManager.replyRepository.findByPostId(postId).map { it.toDto() }
        val pagedReplies = replies.subList(startIndex.coerceAtLeast(0), endIndex.coerceAtMost(replies.size))

        call.respond(pagedReplies)
    }
}