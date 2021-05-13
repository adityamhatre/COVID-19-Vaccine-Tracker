package com.aditya.covid_19vaccinetracker.viewmodels

import androidx.lifecycle.ViewModel
import com.aditya.covid_19vaccinetracker.adapters.ResultsAdapter
import com.aditya.covid_19vaccinetracker.dtos.Center

class MainViewModel : ViewModel() {

    val resultsList = mutableListOf<Center>()
    val adapter = ResultsAdapter(resultsList)

}