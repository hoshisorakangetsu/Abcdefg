package com.example.abcdefg.data

import java.util.Date

// TODO Review this
data class Event(
    val name: String  = "",
    val description: String = "",
    val eventDate: Date,
    val eventStartTime: Date,
    val eventEndTime: Date,
    val imgPath: String,
    val joinedBy: List<String>,
)
