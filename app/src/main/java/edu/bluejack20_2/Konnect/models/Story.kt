package edu.bluejack20_2.Konnect.models

import java.time.LocalDateTime

data class Story(
        val id: Int = 0,
        val user: User,
        val media: String = "",
        val createdAt: LocalDateTime,
        val deletedAt: LocalDateTime
)
