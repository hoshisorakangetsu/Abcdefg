package com.example.abcdefg.data

import com.google.firebase.Timestamp
import java.util.Date

data class Topic(
    val title: String = "",
    val content: String = "",
    val createdAt: Timestamp = Timestamp.now(),
    val createdBy: String = "",
    val groupId: String = ""
)
