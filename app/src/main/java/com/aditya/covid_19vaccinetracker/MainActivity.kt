package com.aditya.covid_19vaccinetracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aditya.covid_19vaccinetracker.fragments.MainFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
//        https://stackoverflow.com/a/32758447/1892496
    }
}