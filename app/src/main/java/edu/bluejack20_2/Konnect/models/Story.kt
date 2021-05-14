package edu.bluejack20_2.Konnect.models

import java.time.LocalDateTime
import java.util.*

data class Story(
        val id: String = "",
        val user: User = User(),
        val media: String = "",
        val createdAt: Date = Date(),
        val deletedAt: Date = Date()
)
