package com.orost.sampleapp.viewmodel

import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {
    open fun onViewCreated() {}
}