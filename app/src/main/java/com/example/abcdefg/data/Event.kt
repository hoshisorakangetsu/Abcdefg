package com.example.abcdefg.data

import java.util.Calendar
import java.util.Date

// TODO Review this
data class Event(
    val name: String  = "",
    val description: String = "",
    val eventDate: Date = Calendar.getInstance().time,
    val eventStartTime: Date = Calendar.getInstance().time,
    val eventEndTime: Date = Calendar.getInstance().time,
    val groupId: String = "",
    val createdBy: String = "",
    val imgPath: String = "",
    val joinedBy: List<String> = listOf(),
)
