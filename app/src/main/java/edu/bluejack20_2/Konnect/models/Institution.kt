package edu.bluejack20_2.Konnect.models

import com.google.common.collect.ImmutableList

data class Institution(
        val id: String = "",
        var name: String = "",
        var city: City = City(),
        var photoUrl: String = "",
        var summary: String = "",
        var website: String = "",
        var employees: MutableList<User> = mutableListOf<User>()
)
