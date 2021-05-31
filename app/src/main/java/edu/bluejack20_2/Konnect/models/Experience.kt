package edu.bluejack20_2.Konnect.models

import com.google.firebase.Timestamp

data class Experience(
    var id: String = "",
    var institution: Institution = Institution(),
    var title: String = "",
    var employmentType: EmploymentType = EmploymentType(),
    var startDate: Timestamp = Timestamp.now(),
    var isWorking: Boolean = false,
    var endDate: Timestamp = Timestamp.now(),
    var description: String = ""
)