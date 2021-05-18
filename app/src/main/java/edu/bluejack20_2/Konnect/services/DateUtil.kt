package edu.bluejack20_2.Konnect.services

import com.google.firebase.Timestamp
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DateUtil {

    public fun timestampToStandardTime(timestamp:Timestamp): String {
        val date = timestamp.toDate()
        val dateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

        val formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")
        return dateTime.format(formatter)
    }

    public fun timestampToYear(timestamp: Timestamp): Int {
        val date = timestamp.toDate()
        val dateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

        val formatter = DateTimeFormatter.ofPattern("yyyy")
        return dateTime.format(formatter).toInt()
    }

    public fun timestampToMonthYear(timestamp: Timestamp): String {
        val date = timestamp.toDate()
        val dateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

        val formatter = DateTimeFormatter.ofPattern("MM-yyyy")
        return dateTime.format(formatter)
    }
}