package com.example.interfaces

import com.example.data.models.topic.Topic

interface MasterRepository: Repository<Topic> {
    fun getAllData(userId: String): List<Topic>
}