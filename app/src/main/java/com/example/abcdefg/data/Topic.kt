package com.example.abcdefg.data

import java.util.Date

data class Topic(val title: String, val content: String, val createdAt: Date, val createdBy: User, val replies: Array<Reply>)
