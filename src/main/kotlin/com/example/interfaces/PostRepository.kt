package com.example.interfaces

import com.example.data.models.post.Post

interface PostRepository: Repository<Post> {
    fun findByThreadId(id: String, userId: String): List<Post>
    fun approvePost(postId: String): Any
    fun updateById(id: String, request: Post): Boolean
}