package com.example.routes

import com.example.data.models.ErrorResponse
import com.example.data.models.JwtTokenConfig
import com.example.data.models.Request.EditPrivilegedUserRolesRequest
import com.example.data.models.Response.AdminLoginResponse
import com.example.data.models.privilegedUser.PrivilegedUser
import com.example.data.models.privilegedUser.PrivilegedUserDTO
import com.example.data.models.privilegedUser.toDTO
import com.example.data.models.user.toDto
import com.example.plugins.jwtAuthService
import com.example.services.BCryptService
import com.example.services.MongoDBManager
import com.example.services.MongoDBManager.privilegedUserRepository
import com.example.utils.LOGIN_SUCCESS
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId

fun Route.getPrivilegedUsersRoute() {
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

fun Route.addPrivilegedUserRoute() {
    post {
        val request = call.receive<PrivilegedUserDTO>()
        val email = request.email
        val password = request.password
        val roles = request.roles

        if (isEmailIdValid(email) && isPasswordValid(password)) {

            val hashedPassword = BCryptService.createHashedPassword(password)

            val user = createPrivilegedUser(email, hashedPassword, roles)
            val userCreated = createPrivilegedUserIfNotExists(user)

            if (userCreated is Boolean) {
                val userData = privilegedUserRepository.findByEmail(email)
                userData?.let {
                    call.respond("Email already used")
                } ?: call.respond(HttpStatusCode.InternalServerError, "User not found")
            }else if(userCreated is PrivilegedUser){
                call.respond("New user created")
            }
        } else {
            call.respond(HttpStatusCode.BadGateway, "Invalid email or password")
        }
    }
}

fun Route.editPrivilegedUserRolesRoute(){
    put("/{id}") {
        val request = call.receive<EditPrivilegedUserRolesRequest>()
        val id = call.parameters["id"].toString()
        val roles = request.roles
        val isUpdated = privilegedUserRepository.updateRoles(id, roles)
        if(isUpdated){
            call.respond("Roles updated")
        }else{
            call.respond(HttpStatusCode.NotFound, "User not found")
        }

    }
}

suspend fun generateTokens(user: PrivilegedUser, jwtAccessTokenConfig: JwtTokenConfig, jwtRefreshTokenConfig: JwtTokenConfig,
                           call: ApplicationCall, email: String){
    val privilegedUserDto = user.toDTO()
    val refreshToken = jwtAuthService.generateRefreshToken(privilegedUserDto, jwtRefreshTokenConfig)
    val accessToken = jwtAuthService.generateToken(privilegedUserDto, jwtAccessTokenConfig)
    val refreshTokenUpdated = addRefreshToken(refreshToken, ObjectId(privilegedUserDto.id))

    if(refreshTokenUpdated){
        call.respond(
            AdminLoginResponse(
                email = privilegedUserDto.email,
                accessToken = accessToken,
                refreshToken = refreshToken,
                message = LOGIN_SUCCESS
            )
        )
    } else {
        call.respond(HttpStatusCode.InternalServerError, ErrorResponse.INTERNAL_SERVER_ERROR_RESPONSE)
    }
}

fun createPrivilegedUser(email: String, password: String, roles: ArrayList<String>): PrivilegedUser {
    return PrivilegedUser(
        email = email,
        password = password,
        roles = roles
    )
}

fun isEmailIdValid(email: String?): Boolean {
    return email?.isNotEmpty() == true
}
fun isPasswordValid(password: String?): Boolean {
    return password?.isNotEmpty() == true
}

fun createPrivilegedUserIfNotExists(user: PrivilegedUser): Any {
    return privilegedUserRepository.createPrivilegedUserIfNotExists(user)
}