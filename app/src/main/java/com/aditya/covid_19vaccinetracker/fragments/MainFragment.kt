package com.aditya.covid_19vaccinetracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.aditya.covid_19vaccinetracker.R
import com.aditya.covid_19vaccinetracker.viewmodels.MainViewModel
import com.savvyapps.togglebuttonlayout.Toggle
import com.savvyapps.togglebuttonlayout.ToggleButtonLayout
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    val viewModel: MainViewModel by viewModels()

    private var pincodeLayout: PincodeLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resultsView.adapter = viewModel.adapter

        searchToggle.setToggled(R.id.toggle_pincode, true)
        toggleListener(searchToggle, searchToggle.toggles[0], true)
        searchToggle?.onToggledListener = toggleListener
    }

    private val toggleListener =
        toggleListener@{ _: ToggleButtonLayout, toggle: Toggle, isSelected: Boolean ->
            if (!isSelected) {
                return@toggleListener
            }
            when (toggle.id) {
                R.id.toggle_pincode -> {
                    println("Pincode")
                    changeLayoutTo(pincodeLayout())
                }
               /* R.id.toggle_district -> {
                    println("District")
                    changeLayoutTo(DistrictLayout.newInstance("", ""))
                }*/
                else -> {
                }
            }

        }

    private fun pincodeLayout(): Fragment {
        if (pincodeLayout == null) {
            pincodeLayout = PincodeLayout.newInstance("", "")
        }
        return pincodeLayout!!
    }

    private fun changeLayoutTo(fragment: Fragment) {
        if(fragment.isAdded) return
        childFragmentManager
            .beginTransaction()
            .replace(R.id.search_container, fragment)
            .commit()
    }

}

