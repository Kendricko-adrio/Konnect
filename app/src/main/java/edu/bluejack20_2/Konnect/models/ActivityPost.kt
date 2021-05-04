package edu.bluejack20_2.Konnect.models

import com.google.common.collect.ImmutableList
import org.w3c.dom.Comment
import java.time.LocalDateTime

data class ActivityPost(
        val id: Int = 0,
        val user: User,
        var content: String = "",
        var media: String = "",
        var likes: ImmutableList<PostLike>,
        var comments: ImmutableList<Comment>,
        val createdAt: LocalDateTime,
        val deletedAt: LocalDateTime
)
