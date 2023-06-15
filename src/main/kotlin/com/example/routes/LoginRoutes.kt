package com.example.routes

import com.example.data.models.AdminUser
import com.example.data.models.ErrorResponse
import com.example.data.models.JwtTokenConfig
import com.example.data.models.Request.AdminLoginRequest
import com.example.data.models.Request.OtpRequest
import com.example.data.models.Request.RefreshTokenRequest
import com.example.data.models.Response.AdminLoginResponse
import com.example.data.models.Response.OtpResponse
import com.example.data.models.Response.RefreshTokenResponse
import com.example.data.models.Role
import com.example.data.models.user.toDto
import com.example.plugins.jwtAuthService
import com.example.services.BCryptService
import com.example.services.MongoDBManager
import com.example.utils.LOGIN_SUCCESS
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.operatorLoginRoute(){
    post("/otp") {
        val request = call.receive<OtpRequest>()
        val phoneNo = request.phoneNumber

        val isValid = phoneNo.isNotEmpty() && (phoneNo.length == 10 || phoneNo.length == 12)

        if(isValid){
            call.respond(
                OtpResponse(
                otp = 1234,
                message = "Otp has been sent to your mobile number"
            )
            )
        } else
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid Phone number"))
    }
}

fun Route.adminLoginRoute(jwtAccessTokenConfig: JwtTokenConfig, jwtRefreshTokenConfig: JwtTokenConfig){
    post("/admin/login"){
        val request = call.receive<AdminLoginRequest>()
        val email = request.email
        val password = request.password

        if(isEmailIdValid(email) && isPasswordValid(password)){

            val privilegedUser = MongoDBManager.privilegedUserRepository.findByEmail(email)

            privilegedUser?.let {

                if(BCryptService.verifyPassword(password, it.password)){
                    generateTokens(it, jwtAccessTokenConfig, jwtRefreshTokenConfig, call, email)
                }else{
                    call.respond(HttpStatusCode.BadRequest, "Incorrect password")
                }

            } ?: call.respond(HttpStatusCode.BadRequest, "Email not found")

        } else {
            call.respond(HttpStatusCode.BadRequest, "Invalid email or password")
        }
    }
}

fun Route.refreshToken(jwtAccessTokenConfig: JwtTokenConfig, jwtRefreshTokenConfig: JwtTokenConfig){
    post("/refreshToken"){
        val request = call.receive<RefreshTokenRequest>()
        val refreshToken = request.refreshToken

        if(refreshToken.isNotEmpty()){
            MongoDBManager.refreshTokenRepository.findByRefreshToken(refreshToken)?.let { refreshTokenData ->

                MongoDBManager.userRepository.findById(refreshTokenData.userId.toString())?.let { user ->

                    val isValidToken = jwtAuthService.verifyToken(refreshToken, jwtRefreshTokenConfig)
                    if(isValidToken){
                        val newAccessToken = jwtAuthService.generateToken(user.toDto(), jwtAccessTokenConfig)
                        call.respond(
                            RefreshTokenResponse(
                            accessToken = newAccessToken
                        )
                        )
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