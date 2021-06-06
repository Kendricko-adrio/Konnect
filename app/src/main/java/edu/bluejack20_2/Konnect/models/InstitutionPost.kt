package edu.bluejack20_2.Konnect.models

import com.google.firebase.Timestamp


data class InstitutionPost (
    var id: String = "",
    var institution: Institution = Institution(),
    var content: String = "",
    var media: String = "",
    var createdAt: Timestamp = Timestamp.now()
)