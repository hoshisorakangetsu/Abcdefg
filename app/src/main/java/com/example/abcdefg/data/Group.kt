package com.example.abcdefg.data

data class Group(
    val name: String = "",
    val desc: String = "",
    val ownerUid: String = "",
    val memberUids: ArrayList<String> = arrayListOf(),
    val tags: ArrayList<String> = arrayListOf(),
    val imgPath: String = "",
)
