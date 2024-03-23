package com.example.abcdefg.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.abcdefg.data.Event

class EventViewModel : ViewModel() {
    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event> = _event
    fun changeEvent(ev: Event) {
        _event.value = ev
    }
}