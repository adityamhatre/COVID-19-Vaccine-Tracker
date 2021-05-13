package com.aditya.covid_19vaccinetracker

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import com.aditya.covid_19vaccinetracker.apis.Cowin
import com.aditya.covid_19vaccinetracker.dtos.CowinResponse
import com.aditya.covid_19vaccinetracker.services.OnAlarmReceiver
import com.aditya.covid_19vaccinetracker.services.RetrofitProvider
import com.aditya.covid_19vaccinetracker.utils.NOTIFICATION_INTERVAL
import com.aditya.covid_19vaccinetracker.utils.getFormattedDate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Covid19VaccineTracker : Application() {
    private val cowinApi: Cowin = RetrofitProvider.create(
        service = Cowin::class.java,
        baseUrl = "https://cdn-api.co-vin.in/api/v2/"
    )

    fun updateSharedPrefs(shouldNotify: Boolean, value: String, searchType: String) {
        getSharedPreferences("keepMeNotified", Context.MODE_PRIVATE)
            .edit()
            .putBoolean("shouldNotify", shouldNotify)
            .putString("value", value)
            .putString("searchType", searchType)
            .apply()
        if (shouldNotify) {
            val i = Intent(this, OnAlarmReceiver::class.java)
            i.action = "com.adityamnhatre.intents.action.CHECK_AVAILABILITY"
            val pi = PendingIntent.getBroadcast(this, 0, i, 0)
            this.getSystemService(AlarmManager::class.java)
                .setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis(),
                    NOTIFICATION_INTERVAL,
                    pi
                )
        }
    }

    fun shouldNotify(): Boolean =
        getSharedPreferences("keepMeNotified", Context.MODE_PRIVATE).getBoolean(
            "shouldNotify",
            false
        )

    fun searchType() =
        getSharedPreferences("keepMeNotified", Context.MODE_PRIVATE).getString(
            "searchType", ""
        )

    private fun getSearchValue(): String {
        return getSharedPreferences("keepMeNotified", Context.MODE_PRIVATE).getString(
            "value", ""
        )!!
    }

    fun notifyIfAvailable(function: (CowinResponse?) -> Unit) {
        if (instance?.shouldNotify() == true) {
            when (instance?.searchType()!!) {
                "pincode" -> {
//                    println("checking for pincode")
//                    println(instance?.getSearchValue()!!)
               /*     var json =
                        """
           {
             "centers": [
               {
                 "center_id": 694639,
                 "name": "Central Rail Off Waiting Room",
                 "address": "Platform No 18 CST Station  P Demello Road CST Mumbai - ${instance?.getSearchValue()!!}",
                 "state_name": "Maharashtra",
                 "district_name": "Mumbai",
                 "block_name": "Ward A Corporation - MH",
                 "pincode": 400001,
                 "lat": 18,
                 "long": 72,
                 "from": "09:00:00",
                 "to": "17:00:00",
                 "fee_type": "Free",
                 "sessions": [
                   {
                     "session_id": "bc1f2bc9-f695-41bb-a298-db6a1f7848c3",
                     "date": "11-05-2021",
                     "available_capacity": 99,
                     "min_age_limit": 45,
                     "vaccine": "COVISHIELD",
                     "slots": [
                       "09:00AM-11:00AM",
                       "11:00AM-01:00PM",
                       "01:00PM-03:00PM",
                       "03:00PM-05:00PM"
                     ]
                   },
                   {
                     "session_id": "bc1f2bc9-f695-41bb-a298-db6a1f7848c3",
                     "date": "11-05-2021",
                     "available_capacity": 5,
                     "min_age_limit": 45,
                     "vaccine": "COVAXINE",
                     "slots": [
                       "09:00AM-11:00AM",
                       "11:00AM-01:00PM",
                       "01:00PM-03:00PM",
                       "03:00PM-05:00PM"
                     ]
                   },
                   {
                     "session_id": "bc1f2bc9-f695-41bb-a298-db6a1f7848c3",
                     "date": "11-05-2021",
                     "available_capacity": 0,
                     "min_age_limit": 45,
                     "vaccine": "IGNORE",
                     "slots": [
                       "09:00AM-11:00AM",
                       "11:00AM-01:00PM",
                       "01:00PM-03:00PM",
                       "03:00PM-05:00PM"
                     ]
                   }
                 ]
               },
               
               {
                 "center_id": 694639,
                 "name": "2nd center",
                 "address": "2nd center Demello Road CST Mumbai - ${instance?.getSearchValue()!!}",
                 "state_name": "Maharashtra",
                 "district_name": "Mumbai",
                 "block_name": "Ward A Corporation - MH",
                 "pincode": 400001,
                 "lat": 18,
                 "long": 72,
                 "from": "09:00:00",
                 "to": "17:00:00",
                 "fee_type": "Free",
                 "sessions": [
                   {
                     "session_id": "bc1f2bc9-f695-41bb-a298-db6a1f7848c3",
                     "date": "11-05-2021",
                     "available_capacity": 1,
                     "min_age_limit": 45,
                     "vaccine": "COVISHIELD",
                     "slots": [
                       "09:00AM-11:00AM",
                       "11:00AM-01:00PM",
                       "01:00PM-03:00PM",
                       "03:00PM-05:00PM"
                     ]
                   },
                   {
                     "session_id": "bc1f2bc9-f695-41bb-a298-db6a1f7848c3",
                     "date": "11-05-2021",
                     "available_capacity": 5,
                     "min_age_limit": 45,
                     "vaccine": "COVAXINE",
                     "slots": [
                       "09:00AM-11:00AM",
                       "11:00AM-01:00PM",
                       "01:00PM-03:00PM",
                       "03:00PM-05:00PM"
                     ]
                   },
                   {
                     "session_id": "bc1f2bc9-f695-41bb-a298-db6a1f7848c3",
                     "date": "11-05-2021",
                     "available_capacity": 0,
                     "min_age_limit": 45,
                     "vaccine": "IGNORE",
                     "slots": [
                       "09:00AM-11:00AM",
                       "11:00AM-01:00PM",
                       "01:00PM-03:00PM",
                       "03:00PM-05:00PM"
                     ]
                   }
                 ]
               }
             ]
           }
        """
                    json = """
                        {
                            "centers": []
                        }
                    """*/
                    getCowinService()
                        .getCentersWithAvailability(
                            instance?.getSearchValue()!!,
                            getFormattedDate()
                        )
                        .enqueue(
                            object : Callback<CowinResponse> {
                                override fun onResponse(
                                    call: Call<CowinResponse>,
                                    cowinResponse: Response<CowinResponse>
                                ) {
                                    if (cowinResponse.code() != 200) {
                                        return
                                    }
                                    val centers = cowinResponse.body()
                                    function(centers)
                                }

                                override fun onFailure(call: Call<CowinResponse>, t: Throwable) {

                                }
                            })
                    /* val centers = Gson().fromJson<CowinResponse>(
                         json.trimIndent(), CowinResponse::class.java
                     )
                     function(centers)*/
                    /*getCowinService().getCentersWithAvailability(
                        instance?.getSearchValue()!!,
                        getFormattedDate()
                    ).enqueue(object :Callback<CowinResponse>{
                        override fun onResponse(
                            call: Call<CowinResponse>,
                            cowinResponse: Response<CowinResponse>
                        ) {
                            if (cowinResponse.code() != 200) {
                                return
                            }
                            function(cowinResponse.body()!!)
                        }

                        override fun onFailure(call: Call<CowinResponse>, t: Throwable) {
                            function(null)
                        }

                    })*/
                }
                "district" -> return
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        createNotificationChannel()

    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name = getString(R.string.channel_name)
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannelGroup(
                NotificationChannelGroup(
                    getString(R.string.notification_group_id),
                    name
                )
            )

            val descriptionText = getString(R.string.channel_name)

            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel =
                NotificationChannel(getString(R.string.CHANNEL_ID), name, importance).apply {
                    description = descriptionText
                    setShowBadge(true)
                    group = getString(R.string.notification_group_id)
                }

            notificationManager.createNotificationChannel(channel)
        }
    }

    fun getPincodeAndNotifyStateIfAny(): Pair<String?, Boolean> {
        return Pair(if (searchType().equals("pincode")) getSearchValue() else null, shouldNotify())
    }

    companion object {
        private var instance: Covid19VaccineTracker? = null
        fun getCowinService() = instance?.cowinApi!!
        fun getInstance() = instance!!
    }
}