package com.example.abcdefg.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GroupViewModel : ViewModel() {

    // rn using the name of the group, havent rlly decided how to store the data yet
    private val _activeGroupId: MutableLiveData<String> = MutableLiveData("")
    val activeGroupId: LiveData<String> = _activeGroupId
    fun navigateToNewGroup(newGroupId: String) {
        _activeGroupId.value = newGroupId
    }

    companion object {
        enum class GroupMainFragments { CHAT, TOPIC_LIST, TOPIC_CONTENT, EVENT }
    }
    private val _activeFragmentType: MutableLiveData<GroupMainFragments> = MutableLiveData(GroupMainFragments.CHAT)
    val activeFragmentType: LiveData<GroupMainFragments> = _activeFragmentType
    fun navigateToNewFragment(newFragmentType: GroupMainFragments) {
        _activeFragmentType.value = newFragmentType
    }

}