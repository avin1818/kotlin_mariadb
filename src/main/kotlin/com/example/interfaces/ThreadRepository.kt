package com.example.interfaces

import com.example.data.models.thread.Thread

interface ThreadRepository: Repository<Thread> {
    fun findByTopicId(id: String): List<Thread>
    fun updateById(id: String, request: Thread): Boolean
}