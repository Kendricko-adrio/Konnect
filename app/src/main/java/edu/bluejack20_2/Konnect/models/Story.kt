package edu.bluejack20_2.Konnect.models

import com.google.firebase.Timestamp
import java.time.LocalDateTime
import java.util.*

data class Story(
        val id: String = "",
        val user: User = User(),
        val media: String = "",
        val createdAt: Timestamp = Timestamp.now(),
        val deletedAt: Timestamp = Timestamp.now()
)
