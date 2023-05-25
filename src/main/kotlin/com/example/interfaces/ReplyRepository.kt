package com.example.interfaces

import com.example.data.models.reply.Reply

interface ReplyRepository: Repository<Reply> {
    fun findByPostId(id: String): List<Reply>
}