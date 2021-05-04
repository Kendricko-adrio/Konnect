package edu.bluejack20_2.Konnect.models

import java.util.*

data class TestScore(
        val id: Int = 0,
        var title: String = "",
        var score: Double = 0.0,
        var testDate: Date,
        var description: String = ""
)
