package com.example.abcdefg.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GroupViewModel : ViewModel() {

    // rn using the name of the group, havent rlly decided how to store the data yet
    private val _activeGroupId: MutableLiveData<String> = MutableLiveData("Study Group 1")
    val activeGroupId: LiveData<String> = _activeGroupId

    fun navigateToNewGroup(newGroupId: String) {
        _activeGroupId.value = newGroupId
    }


}