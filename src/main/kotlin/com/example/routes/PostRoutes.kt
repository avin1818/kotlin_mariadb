package com.example.routes

import com.example.data.models.ErrorResponse
import com.example.data.models.Request.CreatePostOrReplyRequest
import com.example.data.models.Request.toPostModel
import com.example.data.models.Request.toReplyModel
import com.example.data.models.post.toDto
import com.example.data.models.reply.toDto
import com.example.data.utils.getClaimValueFromJWT
import com.example.services.MongoDBManager
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getPostsForThreadRoute(){
    get("/{threadId}") {
        val threadId = call.parameters["threadId"].toString()
        val userId = call.getClaimValueFromJWT("id") ?: ""

        val page = call.parameters["page"]?.toIntOrNull() ?: 1
        val pageLimit = call.parameters["limit"]?.toIntOrNull() ?: 10

        val startIndex = (page - 1) * pageLimit
        val endIndex = startIndex + pageLimit

        val posts = MongoDBManager.postRepository.findByThreadId(threadId, userId).map { it.toDto() }
        val pagedPosts = posts.subList(startIndex.coerceAtLeast(0), endIndex.coerceAtMost(posts.size))

        call.respond(pagedPosts)
    }
}

fun Route.createPostOrReplyRoute(){
    post{
        val request = call.receive<CreatePostOrReplyRequest>()
        val userId = call.getClaimValueFromJWT("id") ?: ""

        val isPost = request.postId.isNullOrEmpty()

        MongoDBManager.userRepository.findById(userId)?.let { user ->

            request.apply {
                this.createdBy = user.id.toString()
                this.createdByName = user.username
            }

            if(isPost){
                /**
                 * Create a post
                 */

                val post = request.toPostModel()
                val success = MongoDBManager.postRepository.insert(post)

                if(success){
                    call.respond(post.toDto())
                }else{
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse.BAD_REQUEST_RESPONSE)
                }
            }else{
                /**
                 * Create a reply
                 */
                val reply = request.toReplyModel()
                val success = MongoDBManager.replyRepository.insert(reply)

                if(success){
                    call.respond(reply.toDto())
                }else{
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse.BAD_REQUEST_RESPONSE)
                }
            }

        } ?: call.respond(HttpStatusCode.NotFound, ErrorResponse.USER_NOT_FOUND_RESPONSE)
    }
}

fun Route.approvePostRoute(){
    get("/approve-post/{postId}"){
        val postId = call.parameters["postId"].toString()

        when(val postApproved = MongoDBManager.postRepository.approvePost(postId)){
            is Boolean -> call.respond("Post approved")
            is String -> call.respond(HttpStatusCode.BadRequest, postApproved)
        }
    }
}

fun Route.editPostRoute(){
    put("/{id}"){
        val id = call.parameters["id"].toString()
        val userId = call.getClaimValueFromJWT("id") ?: ""
        val request = call.receive<CreatePostOrReplyRequest>()

        request.apply {
            this.createdBy = userId
        }

        val post = request.toPostModel()

        val postUpdated = MongoDBManager.postRepository.updateById(id, post)

        if(postUpdated){
            call.respond("Post updated")
        }else
            call.respond(HttpStatusCode.BadRequest, ErrorResponse.BAD_REQUEST_RESPONSE)
    }
}

fun Route.deletePostRoute(){
    delete("/{id}"){
        val id = call.parameters["id"].toString()

        val postDeleted = MongoDBManager.postRepository.deleteById(id)

        if(postDeleted){
            call.respond("Post deleted")
        }else
            call.respond(HttpStatusCode.BadRequest, ErrorResponse.BAD_REQUEST_RESPONSE)
    }
}