package edu.bluejack20_2.Konnect.models

import java.util.*

data class Organization(
        val id: Int = 0,
        var name: String = "",
        var positionHeld: String = "",
        var joinDate: Date,
        val description: String = ""
)
