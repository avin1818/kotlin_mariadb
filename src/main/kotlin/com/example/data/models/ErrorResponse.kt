package com.example.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(val message: String){
    companion object {
        val POST_NOT_FOUND_RESPONSE = ErrorResponse("Post not found")
        val USER_NOT_FOUND_RESPONSE = ErrorResponse("User not found")
        val BAD_REQUEST_RESPONSE = ErrorResponse("Invalid Request")
        val INTERNAL_SERVER_ERROR_RESPONSE = ErrorResponse("Something went wrong")
    }
}