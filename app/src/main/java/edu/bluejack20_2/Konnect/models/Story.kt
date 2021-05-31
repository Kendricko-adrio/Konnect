package edu.bluejack20_2.Konnect.models

import com.google.firebase.Timestamp
import java.time.LocalDateTime
import java.util.*

data class Story(
    val id: String = "",
    var user: User = User(),
    var media: String = "",
    var createdAt: Timestamp = Timestamp.now(),
    var deletedAt: Timestamp = Timestamp.now()
)
