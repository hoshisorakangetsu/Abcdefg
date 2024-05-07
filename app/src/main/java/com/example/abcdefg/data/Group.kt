package com.example.abcdefg.data

data class Group(val name: String, val ownerUid: String, val memberUids: List<String>, val tags: List<String>, val grpImgPath: String)

// TODO implement me
fun getAvailableInterestTags(): ArrayList<String> {
    return arrayListOf("IT", "C++", "Java", "Android", "Kotlin")
}
