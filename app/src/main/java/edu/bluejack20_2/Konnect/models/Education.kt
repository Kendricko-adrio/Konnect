package edu.bluejack20_2.Konnect.models

import java.util.*

data class Education(
        val id: String = "",
        var educationDegree: EducationDegree = EducationDegree(),
        var studyField: StudyField = StudyField(),
        var startDate: Date = Date(),
        var endDate: Date = Date(),
        var grade: Double = 0.0,
        var description: String = ""
)
