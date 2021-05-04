package edu.bluejack20_2.Konnect.models

import java.util.*

data class Award(
        val id: Int = 0,
        var title: String = "",
        var issuer: String = "",
        var issueDate: Date,
        var description: String = ""
)
