package com.example.abcdefg.data

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

// https://stackoverflow.com/a/56533638
data class Event(
    val name: String  = "",
    val description: String = "",
    val eventDate: String =  FirestoreDateTimeFormatter.DateFormatter.format(Calendar.getInstance().time),
    val eventStartTime: String = FirestoreDateTimeFormatter.TimeFormatter.format(Calendar.getInstance().time),
    val eventEndTime: String = FirestoreDateTimeFormatter.TimeFormatter.format(Calendar.getInstance().time),
    val groupId: String = "",
    val createdBy: String = "",
    val imgPath: String = "",
    val joinedBy: List<String> = listOf(),
)

class FirestoreDateTimeFormatter {
    @SuppressLint("SimpleDateFormat")
    companion object {
        val DateFormatter = SimpleDateFormat("yyyyMMDD")
        val TimeFormatter = SimpleDateFormat("HHMM")
    }
}
