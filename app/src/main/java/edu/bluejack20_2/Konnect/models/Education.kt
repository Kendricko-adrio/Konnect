package edu.bluejack20_2.Konnect.models

import java.util.*

data class Education(
        val id: String = "",
        var educationDegree: EducationDegree,
        var studyField: StudyField,
        var startDate: Date,
        var endDate: Date,
        var grade: Double = 0.0,
        var description: String = ""
)
