package edu.bluejack20_2.Konnect.models

data class EducationDegree(
    var id: String = "",
    val name: String = ""
) {
    override fun toString(): String {
        return name
    }
}
