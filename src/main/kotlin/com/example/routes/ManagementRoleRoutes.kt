package com.example.routes

import com.example.data.models.ManagementRole
import com.example.services.authorization
import io.ktor.server.routing.*

fun Route.userManagementRoute() {
    route("/users") {
        authorization(arrayListOf(ManagementRole.USER_MANAGER.name))
        getUsersRoute()
        approveUserRoute()
    }

    route("/privileged-users") {
        authorization(arrayListOf(ManagementRole.USER_MANAGER.name))
        getPrivilegedUsersRoute()
        addPrivilegedUserRoute()
        editPrivilegedUserRolesRoute()
    }
}
fun Route.contentManagementRoute() {
    /**
     * Topics CRUD
     */
    route("/topics"){
        authorization(arrayListOf(ManagementRole.CONTENT_MANAGER.name))
        getTopicsRoute()
        addTopicRoute()
        editTopicRoute()
        deleteTopicRoute()
    }

    /**
     * Threads CRUD
     */
    route("/threads"){
        authorization(arrayListOf(ManagementRole.CONTENT_MANAGER.name))
        getThreadsForTopicsRoute()
        addThreadRoute()
        editThreadRoute()
        deleteThreadRoute()
    }

    /**
     * Posts CRUD
     */
    route("/posts"){
        authorization(arrayListOf(ManagementRole.CONTENT_MANAGER.name))
        getPostsForThreadRoute()
        createPostOrReplyRoute()
        editPostRoute()
        deletePostRoute()
        approvePostRoute()
    }

    /**
     * Replies CRUD
     */
    route("/replies"){
        authorization(arrayListOf(ManagementRole.CONTENT_MANAGER.name))
        getRepliesForTopicRoute()
    }
}