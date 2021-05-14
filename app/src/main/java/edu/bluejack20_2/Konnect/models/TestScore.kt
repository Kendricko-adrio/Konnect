package edu.bluejack20_2.Konnect.models

import java.util.*

data class TestScore(
        val id: String = "",
        var title: String = "",
        var score: Double = 0.0,
        var testDate: Date = Date(),
        var description: String = ""
)
