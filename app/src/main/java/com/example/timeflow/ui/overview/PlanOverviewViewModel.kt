package com.example.timeflow.ui.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PlanOverviewViewModel {
    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}