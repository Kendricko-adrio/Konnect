package edu.bluejack20_2.Konnect.models

import java.time.LocalDateTime
import java.util.*

data class PostComment(
        val id: String = "",
        val user: User = User(),
        val post: ActivityPost = ActivityPost(),
        var content: String = "",
        val createdAt: Date = Date(),
        val deletedAt: Date = Date()
)

