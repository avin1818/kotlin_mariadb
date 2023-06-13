package com.example.routes

import com.example.data.models.Role
import com.example.services.roleAuthorization
import io.ktor.server.routing.*

fun Route.adminApiRoutes() {
    route("/api"){
        /**
         * MODERATOR APIS
         * Any api below roleAuthorization() function will require ADMIN/MODERATOR ACCESS
         */
        route("/admin"){
            roleAuthorization(listOf(Role.MODERATOR.name, Role.ADMIN.name))

            getUsersRoute()

            approveUserRoute()

            approvePostRoute()

            /**
             * Topics CRUD
             */
            route("/topics"){
                addTopicRoute()
                editTopicRoute()
                deleteTopicRoute()
            }

            /**
             * Threads CRUD
             */
            route("/threads"){
                addThreadRoute()
                editThreadRoute()
                deleteThreadRoute()
            }

            /**
             * Posts CRUD
             */
            route("/posts"){
                createPostOrReplyRoute()
                editPostRoute()
                deletePostRoute()
            }
        }
    }
}