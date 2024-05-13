package com.example.abcdefg.data

import com.google.firebase.Timestamp
import java.util.Date

data class ChatMessage(
    val content: String = "",
    val sentBy: String = "",
    val groupId: String = "",
    val sentAt: Timestamp = Timestamp.now()
)
