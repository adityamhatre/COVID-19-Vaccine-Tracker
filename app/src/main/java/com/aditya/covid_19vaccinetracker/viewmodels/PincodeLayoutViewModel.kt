package com.aditya.covid_19vaccinetracker.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PincodeLayoutViewModel : ViewModel() {
    private val pincodeInput: MutableLiveData<String> = MutableLiveData("")

    fun getPincodeInput(): LiveData<String> = pincodeInput
    fun setPincodeInput(pincode: String) {
        pincodeInput.value = pincode
    }


}