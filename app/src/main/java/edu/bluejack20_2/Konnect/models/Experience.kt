package edu.bluejack20_2.Konnect.models

import java.util.*

data class Experience(
        val id: String = "",
        var institution: Institution,
        var title: String = "",
        var employmentType: EmploymentType,
        var city: City,
        val startDate: Date,
        var isWorking: Boolean = false,
        val endDate: Date,
        var description: String
)
