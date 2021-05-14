package edu.bluejack20_2.Konnect.models

import java.time.LocalDateTime
import java.util.*

data class PostLike(
        val id: String = "",
        val user: User = User(),
        val post: ActivityPost = ActivityPost(),
        val createdAt: Date = Date()
)
