package edu.bluejack20_2.Konnect.models

import com.google.common.collect.ImmutableList

data class Institution(
    var id: String = "",
    var name: String = "",
    var city: City = City(),
    var photoUrl: String = "",
    var summary: String = "",
    var website: String = "",
    var relations: MutableList<User> = mutableListOf<User>(),
    var admins: MutableList<User> = mutableListOf()
) {
    override fun toString(): String {
        return name
    }
}
