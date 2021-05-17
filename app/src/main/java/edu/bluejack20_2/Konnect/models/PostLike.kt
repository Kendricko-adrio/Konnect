package edu.bluejack20_2.Konnect.models

import com.google.firebase.Timestamp

data class PostLike(
        val id: String = "",
        val user: User = User(),
        val post: ActivityPost = ActivityPost(),
        val createdAt: Timestamp = Timestamp.now()
)
