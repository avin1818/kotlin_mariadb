package com.example

import com.example.data.models.JwtTokenConfig
import com.example.data.models.thread.Thread
import com.example.data.models.topic.Topic
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import com.example.services.DBDummyData
import com.example.services.MongoDBManager

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {

    val jwtAccessTokenConfig = JwtTokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 3600 * 1000, // 1Hr
        secret = System.getenv("JWT_SECRET")
    )

    val jwtRefreshTokenConfig = JwtTokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 3600 * 1000, // 1Hr
        secret = System.getenv("JWT_REFRESH_SECRET")
    )

    configureSerialization()
    configureAuthentication(jwtAccessTokenConfig)
    configureRouting(jwtAccessTokenConfig, jwtRefreshTokenConfig)
}
