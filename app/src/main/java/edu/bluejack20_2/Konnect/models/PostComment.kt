package edu.bluejack20_2.Konnect.models

import java.time.LocalDateTime

data class PostComment(
        val id: Int = 0,
        val user: User,
        val post: ActivityPost,
        var content: String,
        val createdAt: LocalDateTime,
        val deletedAt: LocalDateTime
)

