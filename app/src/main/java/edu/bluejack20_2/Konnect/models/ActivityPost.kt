package edu.bluejack20_2.Konnect.models

import com.google.common.collect.ImmutableList
import org.w3c.dom.Comment
import java.time.LocalDateTime
import java.util.*

data class ActivityPost(
    val id: String = "",
    var user: User = User(),
    var content: String = "",
    var media: String = "",
    var likes: MutableList<PostLike> = mutableListOf<PostLike>(),
    var comments: MutableList<Comment> = mutableListOf<Comment>(),
    val createdAt: Date = Date(),
    val deletedAt: Date = Date()
)
