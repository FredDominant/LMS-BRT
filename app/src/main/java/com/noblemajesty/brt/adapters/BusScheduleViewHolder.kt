package com.noblemajesty.brt.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.noblemajesty.brt.R
import com.noblemajesty.brt.database.entities.BusSchedule

class BusScheduleViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
    fun setValues(busSchedule: BusSchedule) {
        view.findViewById<TextView>(R.id.scheduleBus).text = busSchedule.busName
        view.findViewById<TextView>(R.id.scheduleCost).text = busSchedule.cost
        view.findViewById<TextView>(R.id.scheduleDepartureDate).text = busSchedule.date
        view.findViewById<TextView>(R.id.scheduleDestination).text = busSchedule.destination
        view.findViewById<TextView>(R.id.scheduleFrom).text = busSchedule.from
        view.findViewById<TextView>(R.id.scheduleStatus).text = busSchedule.status
    }
}