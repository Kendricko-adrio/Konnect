package edu.bluejack20_2.Konnect.models

import com.google.firebase.Timestamp

data class TestScore(
        val id: String = "",
        var title: String = "",
        var score: Double = 0.0,
        var testDate: Timestamp = Timestamp.now(),
        var description: String = ""
)
