package com.example.abcdefg.data

data class Group(
    val name: String = "",
    val desc: String = "",
    val ownerUid: String = "",
    val memberUids: List<String> = listOf(),
    val tags: List<String> = listOf(),
    val grpImgPath: String = "",
)

// TODO implement me
fun getAvailableInterestTags(): ArrayList<String> {
    return arrayListOf("IT", "C++", "Java", "Android", "Kotlin")
}
