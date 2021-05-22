package edu.bluejack20_2.Konnect.models

import com.google.firebase.Timestamp

data class ActivityPost(
    var id: String = "",
    var user: User = User(),
    var content: String = "",
    var media: String = "",
    var likes: MutableList<User> = mutableListOf<User>(),
    var comments: MutableList<PostComment> = mutableListOf<PostComment>(),
    var createdAt: Timestamp = Timestamp.now(),
    var deletedAt: Timestamp = Timestamp.now()
)
