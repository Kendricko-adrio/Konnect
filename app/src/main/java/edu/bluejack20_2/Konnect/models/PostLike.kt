package edu.bluejack20_2.Konnect.models

import java.time.LocalDateTime

data class PostLike(
        val id: Int = 0,
        val user: User,
        val post: ActivityPost,
        val createdAt: LocalDateTime
)
