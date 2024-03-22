package com.example.abcdefg.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GroupViewModel : ViewModel() {

    private val _testing: MutableLiveData<String> = MutableLiveData("testing")
    val testing: LiveData<String> = _testing

    fun changeTesting(newString: String) {
        _testing.value = newString
    }


}