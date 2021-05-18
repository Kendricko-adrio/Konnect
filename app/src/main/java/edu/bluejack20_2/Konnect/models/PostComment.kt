package edu.bluejack20_2.Konnect.models

import com.google.firebase.Timestamp

data class PostComment(
        val id: String = "",
        val user: User = User(),
        val post: ActivityPost = ActivityPost(),
        var content: String = "",
        val createdAt: Timestamp = Timestamp.now(),
        val deletedAt: Timestamp = Timestamp.now()
)

