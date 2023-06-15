package com.example.routes

import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.adminApiRoutes() {
    authenticate {
        route("/api/admin"){
            /**
             * USER MANAGEMENT ROLE ONLY
             */
            userManagementRoute()

            /**
             * CONTENT MANAGEMENT ROLE ONLY
             */
            contentManagementRoute()
        }
    }
}