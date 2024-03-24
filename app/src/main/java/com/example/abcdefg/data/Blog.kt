package com.example.abcdefg.data

import java.util.Date

data class Blog(val title: String, val content: String, val interestTags: Array<String>, val createdAt: Date, val createdBy: User)
