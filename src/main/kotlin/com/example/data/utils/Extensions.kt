package com.example.data.utils

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.bson.types.ObjectId
import java.lang.IllegalArgumentException

fun ApplicationCall.getClaimValueFromJWT(claimName: String): String? {
    val principal = authentication.principal<JWTPrincipal>()
    return principal?.payload?.getClaim(claimName)?.asString()
}

fun String.checkIfValidId(): Boolean =
    try {
        ObjectId(this)
        true
    }catch (e: IllegalArgumentException){
        false
    }