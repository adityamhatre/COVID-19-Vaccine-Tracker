package com.aditya.covid_19vaccinetracker.apis

import com.aditya.covid_19vaccinetracker.dtos.CowinResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Cowin {
    @GET("appointment/sessions/public/calendarByPin")
    fun getCentersWithAvailability(
        @Query("pincode") pincode: String,
        @Query("date") date: String
    ): Call<CowinResponse>

}