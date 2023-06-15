package com.example.plugins

import com.example.data.models.JwtTokenConfig
import com.example.routes.adminApiRoutes
import com.example.routes.forumApiRoutes
import com.example.routes.loginApiRoutes
import com.example.services.JwtAuthService
import io.ktor.server.application.*
import io.ktor.server.routing.*

val jwtAuthService = JwtAuthService()

fun Application.configureRouting(jwtAccessTokenConfig: JwtTokenConfig, jwtRefreshTokenConfig: JwtTokenConfig) {

    routing {

        loginApiRoutes(jwtAccessTokenConfig, jwtRefreshTokenConfig)
        forumApiRoutes()
        adminApiRoutes()
    }
}
