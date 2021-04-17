package com.valagroup.demoforaccessibilitybugs

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val buttonState = MutableLiveData<Boolean>().apply { value = true }

    fun toggleButtonState() {
        buttonState.value = !buttonState.value!!
    }

    init {
        Log.d("MainViewModel", "init")
    }
}