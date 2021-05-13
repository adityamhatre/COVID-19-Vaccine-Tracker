package com.aditya.covid_19vaccinetracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aditya.covid_19vaccinetracker.R

class DistrictLayout : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_district_layout, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = DistrictLayout()
    }
}