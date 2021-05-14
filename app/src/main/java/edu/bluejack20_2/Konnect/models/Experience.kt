package edu.bluejack20_2.Konnect.models

import java.util.*

data class Experience(
        val id: String = "",
        var institution: Institution = Institution(),
        var title: String = "",
        var employmentType: EmploymentType = EmploymentType(),
        var city: City = City(),
        val startDate: Date = Date(),
        var isWorking: Boolean = false,
        val endDate: Date = Date(),
        var description: String = ""
)
