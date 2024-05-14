package com.example.abcdefg.data

import com.google.firebase.Timestamp
import java.util.Date

data class Blog(
    val title: String = "",
    val content: String = "",
    val imgPath: String = "",
    val createdAt: Timestamp = Timestamp.now(),
    val createdBy: String = "",
    val savedBy: ArrayList<String> = arrayListOf(),
)
