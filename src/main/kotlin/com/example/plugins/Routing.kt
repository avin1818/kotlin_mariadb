package com.example.plugins

import com.example.data.models.ClaimData
import com.example.data.models.ErrorResponse
import com.example.data.models.JwtTokenConfig
import com.example.data.models.Request.*
import com.example.data.models.Response.LogoutResponse
import com.example.data.models.Response.OtpResponse
import com.example.data.models.Response.RefreshTokenResponse
import com.example.data.models.Response.VerifyOtpResponse
import com.example.data.models.Role
import com.example.data.models.post.PostDto
import com.example.data.models.post.toDto
import com.example.data.models.post.toPostModel
import com.example.data.models.refreshToken.RefreshToken
import com.example.data.models.reply.toDto
import com.example.data.models.thread.ThreadDto
import com.example.data.models.thread.toDto2
import com.example.data.models.thread.toThreadModel
import com.example.data.models.topic.TopicDto
import com.example.data.models.topic.toDto
import com.example.data.models.topic.toDto2
import com.example.data.models.topic.toTopicModel
import com.example.data.models.user.User
import com.example.data.models.user.toDto
import com.example.data.utils.getClaimValueFromJWT
import com.example.services.*
import com.example.services.MongoDBManager.masterRepository
import com.example.services.MongoDBManager.postRepository
import com.example.services.MongoDBManager.refreshTokenRepository
import com.example.services.MongoDBManager.replyRepository
import com.example.services.MongoDBManager.threadRepository
import com.example.services.MongoDBManager.topicRepository
import com.example.services.MongoDBManager.userFavRepository
import com.example.services.MongoDBManager.userRepository
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import org.bson.types.ObjectId

val jwtAuthService = JwtAuthService()

fun Application.configureRouting(jwtAccessTokenConfig: JwtTokenConfig, jwtRefreshTokenConfig: JwtTokenConfig) {

    val moderatorsList = arrayListOf("9111111111", "9222222222", "9333333333")

    routing {
        route("/api"){
            /**
             * OPERATOR Login (with mobile no.)
             */
            post("/otp") {
                val request = call.receive<OtpRequest>()
                val phoneNo = request.phoneNumber

                val isValid = phoneNo.isNotEmpty() && (phoneNo.length == 10 || phoneNo.length == 12)

                if(isValid){
                    call.respond(OtpResponse(
                        otp = 1234,
                        message = "Otp has been sent to your mobile number"
                    ))
                } else
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid Phone number"))
            }

            post("/verify-otp"){
                val request = call.receive<VerifyOtpRequest>()
                val otp = request.otp
                val phoneNumber = request.phoneNumber
                val username = request.username

                val isPhoneNoValid = phoneNumber.isNotEmpty() && (phoneNumber.length == 10 || phoneNumber.length == 12)

                //Hardcoded otp used currently
                if(isPhoneNoValid){
                    if(otp == 1234){

                        println("phoneNumber: $phoneNumber")

                        phoneNumber?.let {

                            val user = User(
                                username = username,
                                phoneNo = it,
                                role = if(moderatorsList.contains(phoneNumber)) Role.MODERATOR.name else Role.OPERATOR.name
                            )

                            println("New user")

                            when(val userCreated = userRepository.createUserIfNotExists(user)) {
                                // Old user
                                is Boolean -> {
                                    userRepository.findByPhoneNo(it)
                                        ?.let { userData ->
                                            // User is already registered
                                            println("User already registered: $userData, userCreated: $userCreated")
                                            val userDto = userData.toDto()

                                            val refreshToken = jwtAuthService.generateRefreshToken(userDto, jwtRefreshTokenConfig)
                                            val accessToken = jwtAuthService.generateToken(userDto, jwtAccessTokenConfig)

                                            val refreshTokenUpdated = refreshTokenRepository.addRefreshToken(RefreshToken(
                                                token = refreshToken,
                                                userId = ObjectId(userDto.id)
                                            ))

                                            if(refreshTokenUpdated){
                                                call.respond(
                                                    VerifyOtpResponse(
                                                        username = userDto.username,
                                                        phoneNo = it,
                                                        accessToken = accessToken,
                                                        refreshToken = refreshToken,
                                                        message = "Login successful"
                                                    )
                                                )
                                            }else
                                                call.respond(HttpStatusCode.InternalServerError, ErrorResponse.INTERNAL_SERVER_ERROR_RESPONSE)

                                        } ?: call.respond(HttpStatusCode.NotFound, ErrorResponse("Phone number not found"))
                                }
                                is User -> {
                                    // New user was created
                                    val userDto = userCreated.toDto()
                                    println("New user was created: $userDto, userCreated: $userCreated")

                                    val refreshToken = jwtAuthService.generateRefreshToken(userDto, jwtRefreshTokenConfig)
                                    val accessToken = jwtAuthService.generateToken(userDto, jwtAccessTokenConfig)

                                    val refreshTokenUpdated = refreshTokenRepository.addRefreshToken(RefreshToken(
                                        token = refreshToken,
                                        userId = ObjectId(userDto.id)
                                    ))

                                    if(refreshTokenUpdated){
                                        call.respond(
                                            VerifyOtpResponse(
                                                username = userDto.username,
                                                phoneNo = it,
                                                accessToken = accessToken,
                                                refreshToken = refreshToken,
                                                message = "Login successful"
                                            )
                                        )
                                    }else
                                        call.respond(HttpStatusCode.InternalServerError, ErrorResponse.INTERNAL_SERVER_ERROR_RESPONSE)
                                }
                            }
                        } ?: call.respond(HttpStatusCode.InternalServerError, "Phone number not found")
                    }else{
                        call.respond(HttpStatusCode.BadGateway,"Invalid Otp")
                    }
                }else
                    call.respond(HttpStatusCode.BadGateway,"Invalid phone number")

            }

            /**
             * Refresh Token
             */
            post("/refreshToken"){
                val request = call.receive<RefreshTokenRequest>()
                val refreshToken = request.refreshToken

                if(refreshToken.isNotEmpty()){
                    refreshTokenRepository.findByRefreshToken(refreshToken)?.let {refreshTokenData ->

                        userRepository.findById(refreshTokenData.userId.toString())?.let {user ->

                            val isValidToken = jwtAuthService.verifyToken(refreshToken, jwtRefreshTokenConfig)
                            if(isValidToken){
                                val newAccessToken = jwtAuthService.generateToken(user.toDto(), jwtAccessTokenConfig)
                                call.respond(RefreshTokenResponse(
                                    accessToken = newAccessToken
                                ))
                            }else{
                                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid refresh token"))
                            }

                        } ?: call.respond(HttpStatusCode.BadRequest, ErrorResponse("User not found"))

                    } ?: call.respond(HttpStatusCode.BadRequest, ErrorResponse("Refresh Token not available"))

                }else{
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse.BAD_REQUEST_RESPONSE)
                }

            }

        }

        authenticate {
            route("/api"){
                /**
                 * TEST
                 */
                get("/fetch-token-data"){
                    val userId = call.getClaimValueFromJWT("id") ?: ""
                    val role = call.getClaimValueFromJWT("role") ?: ""
                    val claimData = ClaimData(userId, role)
                    call.respond(claimData)
                }
                /**
                 * TOPICS
                 */
                route("/topics"){
                    get{
                        val userId = call.getClaimValueFromJWT("id") ?: ""
                        call.respond(topicRepository.getAll(userId).map { it.toDto2() })
                    }
                    post("/markAsFav"){
                        val request = call.receive<MarkAsFavRequest>()
                        val userId = call.getClaimValueFromJWT("id") ?: ""

                        request.userId = userId

                        when(val result = userFavRepository.markAsFav(request)){
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

                /**
                 * THREADS
                 */
                route("/threads"){
                    get("/{topicId}"){
                        val topicId = call.parameters["topicId"].toString()
                        call.respond(threadRepository.findByTopicId(topicId).map { it.toDto2() })
                    }
                }

                /**
                 * POSTS
                 */
                route("/posts"){

                    //Get posts
                    get("/{threadId}") {
                        val threadId = call.parameters["threadId"].toString()
                        val userId = call.getClaimValueFromJWT("id") ?: ""

                        val page = call.parameters["page"]?.toIntOrNull() ?: 1
                        val pageLimit = call.parameters["limit"]?.toIntOrNull() ?: 10

                        val startIndex = (page - 1) * pageLimit
                        val endIndex = startIndex + pageLimit

                        val posts = postRepository.findByThreadId(threadId, userId).map { it.toDto() }
                        val pagedPosts = posts.subList(startIndex.coerceAtLeast(0), endIndex.coerceAtMost(posts.size))

                        call.respond(pagedPosts)
                    }

                    // Create a Post or Reply
                    post{
                        val request = call.receive<CreatePostOrReplyRequest>()
                        val userId = call.getClaimValueFromJWT("id") ?: ""

                        val isPost = request.postId.isNullOrEmpty()

                        userRepository.findById(userId)?.let {user ->

                            request.apply {
                                this.createdBy = user.id.toString()
                                this.createdByName = user.username
                            }

                            if(isPost){
                                /**
                                 * Create a post
                                 */

                                val post = request.toPostModel()
                                val success = postRepository.insert(post)

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
                                val success = replyRepository.insert(reply)

                                if(success){
                                    call.respond(reply.toDto())
                                }else{
                                    call.respond(HttpStatusCode.BadRequest, ErrorResponse.BAD_REQUEST_RESPONSE)
                                }
                            }

                        } ?: call.respond(HttpStatusCode.NotFound, ErrorResponse.USER_NOT_FOUND_RESPONSE)
                    }
                }

                /**
                 * REPLIES
                 */
                route("/replies"){

                    get("/{postId}") {

                        val postId = call.parameters["postId"].toString()

                        val page = call.parameters["page"]?.toIntOrNull() ?: 1
                        val pageLimit = call.parameters["limit"]?.toIntOrNull() ?: 10

                        val startIndex = (page - 1) * pageLimit
                        val endIndex = startIndex + pageLimit

                        val replies = replyRepository.findByPostId(postId).map { it.toDto() }
                        val pagedReplies = replies.subList(startIndex.coerceAtLeast(0), endIndex.coerceAtMost(replies.size))

                        call.respond(pagedReplies)
                    }
                }

                /**
                 * MASTER
                 */
                route("/master"){
                    get{
                        val userId = call.getClaimValueFromJWT("id") ?: ""
                        call.respond(masterRepository.getAllData(userId).map { it.toDto() })
                    }
                }
                /**
                 * Logout
                 */
                route("/logout"){
                    get{
                        val userId = call.getClaimValueFromJWT("id") ?: ""
                        val deleted = refreshTokenRepository.deleteRefreshTokenForId(userId)
                        if(deleted){
                            call.respond(
                                LogoutResponse("Logout successful")
                            )
                        }else
                            call.respond(HttpStatusCode.InternalServerError, ErrorResponse.INTERNAL_SERVER_ERROR_RESPONSE)
                    }
                }
                /**
                 * MODERATOR APIS
                 * Any api below roleAuthorization() function will require MODERATOR ACCESS
                 */
                route("/admin"){
                    roleAuthorization(listOf(Role.MODERATOR.name))
                    /**
                     * Approve User
                     */
                    get("/approve-user/{userId}"){
                        val userId = call.parameters["userId"].toString()

                        when(val userApproved = userRepository.approveUser(userId)){
                            is Boolean -> call.respond("User approved")
                            is String -> call.respond(HttpStatusCode.BadRequest, userApproved)
                        }
                    }

                    /**
                     * Approve Post
                     */
                    get("/approve-post/{postId}"){
                        val postId = call.parameters["postId"].toString()

                        when(val postApproved = postRepository.approvePost(postId)){
                            is Boolean -> call.respond("Post approved")
                            is String -> call.respond(HttpStatusCode.BadRequest, postApproved)
                        }
                    }

                    /**
                     * Topics CRUD
                     */
                    route("/topics"){
                        post {
                            val request = call.receive<TopicDto>()
                            val topic = request.toTopicModel()

                            val topicInserted = topicRepository.insert(topic)

                            if(topicInserted){
                                call.respond("Topic inserted")
                            }else
                                call.respond(HttpStatusCode.BadRequest, ErrorResponse.BAD_REQUEST_RESPONSE)
                        }
                        put("/{id}"){
                            val id = call.parameters["id"].toString()
                            val request = call.receive<TopicDto>()
                            val topic = request.toTopicModel()

                            val topicUpdated = topicRepository.updateById(id, topic)

                            if(topicUpdated){
                                call.respond("Topic updated")
                            }else
                                call.respond(HttpStatusCode.BadRequest, ErrorResponse.BAD_REQUEST_RESPONSE)
                        }
                        delete("/{id}"){
                            val id = call.parameters["id"].toString()

                            val topicDeleted = topicRepository.deleteById(id)

                            if(topicDeleted){
                                call.respond("Topic deleted")
                            }else
                                call.respond(HttpStatusCode.BadRequest, ErrorResponse.BAD_REQUEST_RESPONSE)
                        }
                    }

                    /**
                     * Threads CRUD
                     */
                    route("/threads"){
                        post {
                            val request = call.receive<ThreadDto>()
                            val thread = request.toThreadModel()

                            val threadInserted = threadRepository.insert(thread)

                            if(threadInserted){
                                call.respond("Thread inserted")
                            }else
                                call.respond(HttpStatusCode.BadRequest, ErrorResponse.BAD_REQUEST_RESPONSE)
                        }
                        put("/{id}"){
                            val id = call.parameters["id"].toString()
                            val request = call.receive<ThreadDto>()
                            val thread = request.toThreadModel()

                            val threadUpdated = threadRepository.updateById(id, thread)

                            if(threadUpdated){
                                call.respond("Thread updated")
                            }else
                                call.respond(HttpStatusCode.BadRequest, ErrorResponse.BAD_REQUEST_RESPONSE)
                        }
                        delete("/{id}"){
                            val id = call.parameters["id"].toString()

                            val threadDeleted = threadRepository.deleteById(id)

                            if(threadDeleted){
                                call.respond("Thread deleted")
                            }else
                                call.respond(HttpStatusCode.BadRequest, ErrorResponse.BAD_REQUEST_RESPONSE)
                        }
                    }

                    /**
                     * Posts CRUD
                     */
                    route("/posts"){
                        put("/{id}"){
                            val id = call.parameters["id"].toString()
                            val userId = call.getClaimValueFromJWT("id") ?: ""
                            val request = call.receive<CreatePostOrReplyRequest>()

                            request.apply {
                                this.createdBy = userId
                            }

                            val post = request.toPostModel()

                            val postUpdated = postRepository.updateById(id, post)

                            if(postUpdated){
                                call.respond("Post updated")
                            }else
                                call.respond(HttpStatusCode.BadRequest, ErrorResponse.BAD_REQUEST_RESPONSE)
                        }
                        delete("/{id}"){
                            val id = call.parameters["id"].toString()

                            val postDeleted = postRepository.deleteById(id)

                            if(postDeleted){
                                call.respond("Post deleted")
                            }else
                                call.respond(HttpStatusCode.BadRequest, ErrorResponse.BAD_REQUEST_RESPONSE)
                        }
                    }
                }
            }
        }
    }
}
