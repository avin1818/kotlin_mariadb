package com.example.routes

import com.example.data.models.ErrorResponse
import com.example.data.models.JwtTokenConfig
import com.example.data.models.Request.VerifyOtpRequest
import com.example.data.models.Response.VerifyOtpResponse
import com.example.data.models.Role
import com.example.data.models.refreshToken.RefreshToken
import com.example.data.models.user.User
import com.example.data.models.user.toDto
import com.example.plugins.jwtAuthService
import com.example.services.MongoDBManager
import com.example.utils.LOGIN_SUCCESS
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId

fun Route.verifyOtpRoute(jwtAccessTokenConfig: JwtTokenConfig, jwtRefreshTokenConfig: JwtTokenConfig){
    post("/verify-otp"){
        val request = call.receive<VerifyOtpRequest>()
        val otp = request.otp
        val phoneNumber = request.phoneNumber
        val username = request.username

        val isPhoneNoValid = isPhoneNumberValid(phoneNumber)

        if (isPhoneNoValid && isOtpValid(otp)) {
            val user = createUser(username, phoneNumber)
            val userCreated = createUserIfNotExists(user)

            if (userCreated is Boolean) {
                val userData = findUserByPhoneNo(phoneNumber)
                userData?.let {
                    val userDto = userData.toDto()
                    val refreshToken = jwtAuthService.generateRefreshToken(userDto, jwtRefreshTokenConfig)
                    val accessToken = jwtAuthService.generateToken(userDto, jwtAccessTokenConfig)
                    val refreshTokenUpdated = addRefreshToken(refreshToken, ObjectId(userDto.id))

                    if (refreshTokenUpdated) {
                        call.respond(
                            VerifyOtpResponse(
                                username = userDto.username,
                                phoneNo = phoneNumber,
                                accessToken = accessToken,
                                refreshToken = refreshToken,
                                message = LOGIN_SUCCESS
                            )
                        )
                    } else {
                        call.respond(HttpStatusCode.InternalServerError, ErrorResponse.INTERNAL_SERVER_ERROR_RESPONSE)
                    }
                } ?: call.respond(HttpStatusCode.InternalServerError, "Phone number not found")
            }
        } else {
            call.respond(HttpStatusCode.BadGateway, getErrorMessage(isPhoneNoValid, otp))
        }
    }
}

fun isPhoneNumberValid(phoneNumber: String?): Boolean {
    return phoneNumber?.isNotEmpty() == true && (phoneNumber.length == 10 || phoneNumber.length == 12)
}

fun isOtpValid(otp: Int): Boolean {
    return otp == 1234
}

fun createUser(username: String, phoneNumber: String): User {
    return User(
        username = username,
        phoneNo = phoneNumber,
        role = Role.OPERATOR.name
    )
}

fun createUserIfNotExists(user: User): Any {
    return MongoDBManager.userRepository.createUserIfNotExists(user)
}

fun findUserByPhoneNo(phoneNumber: String): User? {
    return MongoDBManager.userRepository.findByPhoneNo(phoneNumber)
}

fun addRefreshToken(refreshToken: String, userId: ObjectId): Boolean {
    return MongoDBManager.refreshTokenRepository.addRefreshToken(
        RefreshToken(
            token = refreshToken,
            userId = userId
        )
    )
}

fun getErrorMessage(isPhoneNoValid: Boolean, otp: Int): String {
    return if (!isPhoneNoValid) {
        "Invalid phone number"
    } else {
        "Invalid OTP: $otp"
    }
}