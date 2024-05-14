package com.example.abcdefg.data

import com.google.firebase.Timestamp
import java.util.Date

data class Reply(
    val content: String = "",
    val createdAt: Timestamp = Timestamp.now(),
    val createdBy: String = ""
)
