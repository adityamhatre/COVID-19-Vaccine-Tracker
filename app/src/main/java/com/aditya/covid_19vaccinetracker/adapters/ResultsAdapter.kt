package com.aditya.covid_19vaccinetracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aditya.covid_19vaccinetracker.R
import com.aditya.covid_19vaccinetracker.dtos.Center
import com.aditya.covid_19vaccinetracker.dtos.Session

class ResultsAdapter(private val centers: List<Center>) :
    RecyclerView.Adapter<ResultsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.results_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(position)

    override fun getItemCount() = centers.size


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            itemView.findViewById<TextView>(R.id.title).text = centers[position].name
            itemView.findViewById<TextView>(R.id.address).text = centers[position].address
            itemView.findViewById<TextView>(R.id.availabilityCount).text =
                getAvailabilities(centers[position].sessions)

        }

        private fun getAvailabilities(sessions: List<Session>): String {
            return sessions.joinToString("\n\n") { "Date: ${it.date}\nSlots: ${it.slots.joinToString { i -> i }}\nAvailability: ${it.availableCapacity}\nMinimum age limit: ${it.minAgeLimit}" }
        }

    }

}