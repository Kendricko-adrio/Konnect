package edu.bluejack20_2.Konnect.models

data class EmploymentType(
        var id: String = "",
        var name: String = ""
) {
        override fun toString(): String {
                return name
        }
}
