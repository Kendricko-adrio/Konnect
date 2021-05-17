package edu.bluejack20_2.Konnect.models

import com.google.firebase.Timestamp

data class Education(
        val id: String = "",
        var educationDegree: EducationDegree = EducationDegree(),
        var institution: Institution = Institution(),
        var studyField: StudyField = StudyField(),
        var startDate: Timestamp = Timestamp.now(),
        var endDate: Timestamp = Timestamp.now(),
        var grade: Double = 0.0,
        var description: String = ""
)
