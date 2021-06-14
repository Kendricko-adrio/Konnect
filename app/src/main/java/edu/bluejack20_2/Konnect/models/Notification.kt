package edu.bluejack20_2.Konnect.models

import com.google.firebase.Timestamp

data class Notification(
    var id: String = "",
    var user: User = User(),
    var relatedUser: User = User(),
    var title: String = "",
    var content: String = "",
    var createdAt: Timestamp = Timestamp.now(),
    var type: String = "",
    var postId: String = ""
)