package com.example

import com.example.data.models.JwtTokenConfig
import com.example.plugins.configureAuthentication
import com.example.plugins.configureCORS
import com.example.plugins.configureRouting
import com.example.plugins.configureSerialization
import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {

    val dotenv: Dotenv = dotenv {
        directory = "."
        filename = ".env"
        ignoreIfMissing = true
    }

    val jwtAccessTokenConfig = JwtTokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 3600 * 1000, // 1Hr
        secret = dotenv["JWT_SECRET"]/*System.getenv("JWT_SECRET")*/
    )

    val jwtRefreshTokenConfig = JwtTokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 3600 * 1000, // 1Hr
        secret = dotenv["JWT_REFRESH_SECRET"]/*System.getenv("JWT_REFRESH_SECRET")*/
    )

    configureCORS()
    configureSerialization()
    configureAuthentication(jwtAccessTokenConfig)
    configureRouting(jwtAccessTokenConfig, jwtRefreshTokenConfig)
}
