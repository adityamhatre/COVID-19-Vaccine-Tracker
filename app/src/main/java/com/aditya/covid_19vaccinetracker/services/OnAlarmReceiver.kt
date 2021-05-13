package com.aditya.covid_19vaccinetracker.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.aditya.covid_19vaccinetracker.Covid19VaccineTracker
import com.aditya.covid_19vaccinetracker.R
import com.aditya.covid_19vaccinetracker.dtos.CowinResponse
import com.aditya.covid_19vaccinetracker.utils.NOTIFICATION_INTERVAL
import com.google.gson.Gson
import java.util.*

class OnAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            val i = Intent(context, OnAlarmReceiver::class.java)
            i.action = "com.adityamnhatre.intents.action.CHECK_AVAILABILITY"
            val pi = PendingIntent.getBroadcast(context, 0, i, 0)
            context.getSystemService(AlarmManager::class.java)
                .setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis(),
                    NOTIFICATION_INTERVAL,
                    pi
                )
        }

        if (intent.action == "com.adityamnhatre.intents.action.CHECK_AVAILABILITY") {
            if (!Covid19VaccineTracker.getInstance().shouldNotify()) {
                val i = Intent(context, OnAlarmReceiver::class.java)
                i.action = "com.adityamnhatre.intents.action.CHECK_AVAILABILITY"
                val pi = PendingIntent.getBroadcast(context, 0, i, 0)
                context.getSystemService(AlarmManager::class.java)
                    .cancel(pi)
            }
            Covid19VaccineTracker.getInstance().notifyIfAvailable { c: CowinResponse? ->
                if (c == null) return@notifyIfAvailable
                println("=================ALARM MANAGER=================")
                var i = 0
                c.centers.filter { it.sessions.any { session -> session.availableCapacity > 0 } }
                    .forEach {
//                        println(it.name)
//                        println(it.address)
                        it.sessions.filter { session -> session.availableCapacity > 0 }
                            .forEach { session ->
//                                println(session)
//                                println("${session.vaccine}\t${session.slots}\t${session.availableCapacity}")
                                val title = "${session.vaccine} vaccine available at ${it.name}"
                                val details =
                                    "$title.\nSlots: ${session.slots.joinToString(", ")}\n${session.availableCapacity} vaccines remaining\n${it.address}"
                                val inputMap: MutableMap<String, String> = HashMap()
                                inputMap["title"] = title
                                inputMap["slots"] = session.slots.joinToString(", ")
                                inputMap["availableCapacity"] = "${session.availableCapacity}"
                                inputMap["address"] = it.address

                                // convert map to JSON String
                                // convert map to JSON String
                                val jsonStr: String = Gson().toJson(inputMap)
                                notify(context, title, details, i, jsonStr)
                                i += 1
                            }
                    }
                println("-----------------------------------")
            }
        }

        if (intent.action?.startsWith("com.adityamnhatre.intents.action.FROM_NOTIFICATION") == true) {
            println(intent.action)
            println(intent.getStringExtra("details"))
            val intent = Intent(context, NotificationDialog::class.java).apply {
                putExtra("details", intent.getStringExtra("details"))
                putExtra("json", intent.getStringExtra("json"))
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            context.startActivity(intent)
        }
    }

    private fun notify(context: Context, title: String, details: String, id: Int, jsonStr: String) {
        // Create an explicit intent for an Activity in your app
        val intent = Intent(context, OnAlarmReceiver::class.java).apply {
            action = "com.adityamnhatre.intents.action.FROM_NOTIFICATION_$id"
            putExtra("details", details)
            putExtra("json", jsonStr)
        }

        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

        val builder = NotificationCompat.Builder(context, context.getString(R.string.CHANNEL_ID))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Vaccine available!")
            .setContentText(title)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(details)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setTimeoutAfter(2 * NOTIFICATION_INTERVAL / 3)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(title, 0, builder.build())
        }
    }

}
