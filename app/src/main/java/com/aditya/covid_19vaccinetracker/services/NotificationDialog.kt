package com.aditya.covid_19vaccinetracker.services

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import androidx.core.view.setPadding
import com.aditya.covid_19vaccinetracker.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class NotificationDialog : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_dialog)
        setFinishOnTouchOutside(true)
        val payload: Map<String, String> = Gson().fromJson(
            intent.getStringExtra("json"),
            object : TypeToken<Map<String, String>>() {}.type
        )
        title = "Vaccine available"
        val s = SpannableStringBuilder()
            .bold { append(payload["title"]) }
            .append("\n")
            .bold { append("Slots: ") }
            .append(payload["slots"])
            .append("\n")
            .bold { append("Available vaccines: ") }
            .append(payload["availableCapacity"])
            .append("\n")
            .bold { append("Address: ") }
            .append(payload["address"])

       /* val textView = TextView(this)
        textView.setPadding(8)
        textView.text = s*/
        AlertDialog.Builder(this)
            .setTitle("Vaccine available")
            .setMessage(s)
            .setPositiveButton(
                "OK"
            ) { _, _ -> finish() }
            .setOnDismissListener { finish() }
            .create()
            .show()
    }
}