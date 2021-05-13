package com.aditya.covid_19vaccinetracker.dtos

import com.google.gson.annotations.SerializedName

data class CowinResponse(val centers: ArrayList<Center>)

data class Center(
    val address: String,

    @SerializedName("block_name")
    val blockName: String,

    @SerializedName("center_id")
    val centerId: String,

    @SerializedName("district_name")
    val districtName: String,

    @SerializedName("fee_type")

    val feeType: String,

    val from: String,

    val lat: Int,

    val long: Int,

    val name: String,

    val pincode: Int,

    val sessions: List<Session>,

    @SerializedName("state_name")
    val stateName: String,

    val to: String
)

data class Session(
    @SerializedName("available_capacity")
    val availableCapacity: Int,

    val date: String,

    @SerializedName("min_age_limit")
    val minAgeLimit: Int,

    @SerializedName("session_id")
    val id: String,

    val slots: ArrayList<String>,

    val vaccine: String
)