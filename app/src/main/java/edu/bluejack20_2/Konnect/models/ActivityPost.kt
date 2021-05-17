package edu.bluejack20_2.Konnect.models

import com.google.firebase.Timestamp
import org.w3c.dom.Comment

data class ActivityPost(
    val id: String = "",
    var user: User = User(),
    var content: String = "",
    var media: String = "",
    var likes: MutableList<PostLike> = mutableListOf<PostLike>(),
    var comments: MutableList<Comment> = mutableListOf<Comment>(),
    var createdAt: Timestamp = Timestamp.now(),
    var deletedAt: Timestamp = Timestamp.now()
)
