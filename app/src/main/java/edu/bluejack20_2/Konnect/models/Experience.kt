package edu.bluejack20_2.Konnect.models

import com.google.firebase.Timestamp

data class Experience(
        val id: String = "",
        var institution: Institution = Institution(),
        var title: String = "",
        var employmentType: EmploymentType = EmploymentType(),
        val startDate: Timestamp = Timestamp.now(),
        var isWorking: Boolean = false,
        val endDate: Timestamp = Timestamp.now(),
        var description: String = ""
)
