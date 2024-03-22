package com.example.abcdefg.data

import java.util.Date

data class ChatMessage(val id: String, val content: String, val sentBy: User, val sentAt: Date)
