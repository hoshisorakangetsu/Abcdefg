package com.example.abcdefg.data

data class Group(val name: String, val tags: Array<String>)

// TODO implement me
fun getAvailableInterestTags(): ArrayList<String> {
    return arrayListOf("IT", "C++", "Java", "Android", "Kotlin")
}
