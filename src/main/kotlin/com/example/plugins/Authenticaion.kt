package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.data.models.ErrorResponse
import com.example.data.models.JwtTokenConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureAuthentication(jwtTokenConfig: JwtTokenConfig) {

    install(Authentication){
        jwt{

            verifier {
                JWT
                    .require(Algorithm.HMAC512(jwtTokenConfig.secret))
                    .withIssuer(jwtTokenConfig.issuer)
                    .build()
            }
            realm = this@configureAuthentication.environment.config.property("jwt.realm").getString()
            validate { cred ->
                if(cred.payload.getClaim("id").asString() != ""){
                    JWTPrincipal(cred.payload)
                }else null
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Token is not valid or has expired"))
            }
        }
    }

}