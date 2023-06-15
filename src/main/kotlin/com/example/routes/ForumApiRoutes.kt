package com.example.routes

import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.forumApiRoutes() {
    authenticate {
        route("/api") {

            /**
             * TEST
             */
            testJWTRoute()

            /**
             * TOPICS
             */
            route("/topics") {
                getTopicsRoute()
                markTopicAsFavRoute()
            }

            /**
             * THREADS
             */
            route("/threads") {
                getThreadsForTopicsRoute()
            }

            /**
             * POSTS
             */
            route("/posts") {
                getPostsForThreadRoute()
                createPostOrReplyRoute()
            }

            /**
             * REPLIES
             */
            route("/replies") {
                getRepliesForTopicRoute()
            }

            /**
             * MASTER
             */
            route("/master") {
                getMasterDataRoute()
            }

            /**
             * Logout
             */
            route("/logout") {
                logoutRoute()
            }
        }
    }
}