package com.valagroup.demoforaccessibilitybugs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    var fragmentCount = MutableLiveData(0)

    fun fragmentOpened() {
        fragmentCount.value = fragmentCount.value!! + 1
    }

    fun fragmentClosed() {
        fragmentCount.value = fragmentCount.value!! - 1
    }
}