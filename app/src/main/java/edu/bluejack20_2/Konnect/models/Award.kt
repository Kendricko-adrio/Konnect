package edu.bluejack20_2.Konnect.models

import com.google.firebase.Timestamp

data class Award(
        val id: Int = 0,
        var title: String = "",
        var issuer: String = "",
        var issueDate: Timestamp = Timestamp.now(),
        var description: String = ""
)
