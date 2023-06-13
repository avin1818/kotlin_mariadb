package com.example.routes

import com.example.data.models.JwtTokenConfig
import io.ktor.server.routing.*

fun Route.loginApiRoutes(jwtAccessTokenConfig: JwtTokenConfig, jwtRefreshTokenConfig: JwtTokenConfig){
    route("/api"){
        /**
         * Test
         */
        testRoute()

        /**
         * ADMIN/MODERATOR Login (with emailId and Password)
         * Currently just creating hardcoded user for admin (not saving in db)
         */
        adminLoginRoute(jwtAccessTokenConfig, jwtRefreshTokenConfig)

        /**
         * OPERATOR Login (with mobile no.)
         */
        operatorLoginRoute()

        /**
         * Verify Otp (OPERATOR)
         */
        verifyOtpRoute(jwtAccessTokenConfig, jwtRefreshTokenConfig)

        /**
         * Refresh Token
         */
        refreshToken(jwtAccessTokenConfig, jwtRefreshTokenConfig)

    }
}