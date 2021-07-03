package edu.bluejack20_2.Konnect.models

import java.util.*

data class EmploymentType(
        var id: String = "",
        var name: String = "",
        var nama: String = ""
) {
        override fun toString(): String {

                if(Locale.getDefault().displayLanguage == "Indonesia"){
                        return nama
                }
                return name
        }
}
