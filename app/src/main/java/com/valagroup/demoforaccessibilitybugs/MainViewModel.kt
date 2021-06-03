package com.valagroup.demoforaccessibilitybugs

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

interface MultiSelectionReceiver<T> {
    fun storeData(data: T)
    fun getData(): MutableLiveData<String>
}

//abstract class MultiSelectionViewModel: ViewModel(), MultiSelectionReceiver<>

open class BaseViewModel<T> : ViewModel(), MultiSelectionReceiver<T> {
    var text = MutableLiveData<String>().apply { value = null }

    override fun storeData(data: T) {
        text.value = data.toString()
    }

    override fun getData(): MutableLiveData<String> {
        return text
    }
}

class MainViewModel<T> : BaseViewModel<T>() {
    val buttonState = MutableLiveData<Boolean>().apply { value = true }

    fun toggleButtonState() {
        buttonState.value = !buttonState.value!!
    }

    init {
        Log.d("MainViewModel", "init")
    }
}