package com.example.interfaces

import com.example.data.models.topic.Topic

interface TopicRepository: Repository<Topic> {
    fun getAll(userId: String): List<Topic>
    fun updateById(id: String, request: Topic): Boolean
}