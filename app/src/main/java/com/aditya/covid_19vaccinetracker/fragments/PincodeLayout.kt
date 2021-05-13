package com.aditya.covid_19vaccinetracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.aditya.covid_19vaccinetracker.Covid19VaccineTracker
import com.aditya.covid_19vaccinetracker.R
import com.aditya.covid_19vaccinetracker.dtos.CowinResponse
import com.aditya.covid_19vaccinetracker.utils.getFormattedDate
import com.aditya.covid_19vaccinetracker.viewmodels.PincodeLayoutViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_pincode_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PincodeLayout : Fragment() {
    private val mainViewModel by lazy { (parentFragment as MainFragment).viewModel }

    private val pincodeLayoutViewModel: PincodeLayoutViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pincode_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)

        pincodeSearchButton.setOnClickListener {
            closeKeyboard()
//            dummydata()
//            return@setOnClickListener
            loadingPincode.visibility = View.VISIBLE
            mainViewModel.resultsList.clear()
            mainViewModel.adapter.notifyDataSetChanged()

            Covid19VaccineTracker.getCowinService()
                .getCentersWithAvailability(pincodeInput.text.toString(), getFormattedDate())
                .enqueue(
                    object : Callback<CowinResponse> {
                        override fun onResponse(
                            call: Call<CowinResponse>,
                            cowinResponse: Response<CowinResponse>
                        ) {
                            loadingPincode.visibility = View.GONE
                            if (cowinResponse.code() != 200) {
                                Toast.makeText(
                                    requireContext(),
                                    "Some error occurred. Status code: ${cowinResponse.code()}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return
                            }
                            val centers = cowinResponse.body()?.centers!!
                            if (centers.size == 0) {
                                Toast.makeText(
                                    requireContext(),
                                    "No availability",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            mainViewModel.resultsList.clear()
                            mainViewModel.resultsList.addAll(centers)
                            mainViewModel.adapter.notifyDataSetChanged()

                        }

                        override fun onFailure(call: Call<CowinResponse>, t: Throwable) {
                            loadingPincode.visibility = View.GONE
                            Toast.makeText(requireContext(), "Error occurred!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    })
        }
    }

    private fun setupView(view: View) {
        prefill()

        updatePincodeButton.setOnClickListener {
            if (pincodeLayoutViewModel.getPincodeInput().value!!.isNotEmpty()) {
                Covid19VaccineTracker.getInstance().updateSharedPrefs(
                    keepMeNotified.isChecked,
                    pincodeLayoutViewModel.getPincodeInput().value!!,
                    "pincode"
                )
                Toast.makeText(
                    requireContext(),
                    "Will now check for ${pincodeLayoutViewModel.getPincodeInput().value!!}",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                pincodeInput.error = "Please enter a pincode"
                keepMeNotified.isChecked = false
            }

        }
        keepMeNotified.setOnCheckedChangeListener { view, isChecked ->
            if (isChecked) {
                updatePincodeButton.visibility = View.VISIBLE
            } else {
                updatePincodeButton.visibility = View.GONE
            }

            if (pincodeLayoutViewModel.getPincodeInput().value!!.isNotEmpty()) {
                if (isChecked) {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Keep me notified")
                        .setMessage("We'll keep checking vaccines for the entered pincode (${pincodeLayoutViewModel.getPincodeInput().value!!}) every 15 minutes and notify you if there's a vaccine available")
                        .setPositiveButton("OK", null)
                        .create().show()
                } else {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Don't keep me notified")
                        .setMessage("We'll stop checking vaccines for the entered pincode")
                        .setPositiveButton("OK", null)
                        .create().show()
                }
                Covid19VaccineTracker.getInstance().updateSharedPrefs(
                    isChecked,
                    pincodeLayoutViewModel.getPincodeInput().value!!,
                    "pincode"
                )
            } else {
                pincodeInput.error = "Please enter a pincode"
                view.isChecked = false
            }
        }
        pincodeInput.setText(pincodeLayoutViewModel.getPincodeInput().value)
        pincodeInput.doOnTextChanged { text, _, _, _ ->
            pincodeLayoutViewModel.setPincodeInput(text.toString())
        }
    }

    private fun prefill() {
        val pincodeAndNotifyStateIfAny =
            Covid19VaccineTracker.getInstance().getPincodeAndNotifyStateIfAny()
        if (pincodeAndNotifyStateIfAny.first == null) {
            return
        }
        pincodeLayoutViewModel.setPincodeInput(pincodeAndNotifyStateIfAny.first!!)
        pincodeInput.setText(pincodeAndNotifyStateIfAny.first!!)
        keepMeNotified.isChecked = pincodeAndNotifyStateIfAny.second

        Covid19VaccineTracker.getInstance().updateSharedPrefs(
            keepMeNotified.isChecked,
            pincodeLayoutViewModel.getPincodeInput().value!!,
            "pincode"
        )

        if (keepMeNotified.isChecked) {
            updatePincodeButton.visibility = View.VISIBLE
        }
    }

    private fun dummydata() {

        val centers = Gson().fromJson<CowinResponse>(
            """
            {"centers":[{"center_id":694639,"name":"Central Rail Off Waiting Room","address":"Platform No 18 CST Station  P Demello Road CST Mumbai -400001","state_name":"Maharashtra","district_name":"Mumbai","block_name":"Ward A Corporation - MH","pincode":400001,"lat":18,"long":72,"from":"09:00:00","to":"17:00:00","fee_type":"Free","sessions":[{"session_id":"bc1f2bc9-f695-41bb-a298-db6a1f7848c3","date":"11-05-2021","available_capacity":99,"min_age_limit":45,"vaccine":"COVISHIELD","slots":["09:00AM-11:00AM","11:00AM-01:00PM","01:00PM-03:00PM","03:00PM-05:00PM"]}]}]}
        """.trimIndent(), CowinResponse::class.java
        )
        mainViewModel.resultsList.clear()
        mainViewModel.resultsList.addAll(centers.centers)
        mainViewModel.adapter.notifyDataSetChanged()
    }

    private fun closeKeyboard() {
        // this will give us the view
        // which is currently focus
        // in this layout
        val view: View? = requireActivity().currentFocus

        // if nothing is currently
        // focus then this will protect
        // the app from crash
        if (view != null) {
            // now assign the system
            // service to InputMethodManager
            val manager: InputMethodManager =
                requireContext().getSystemService(InputMethodManager::class.java)
            manager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = PincodeLayout()
    }
}