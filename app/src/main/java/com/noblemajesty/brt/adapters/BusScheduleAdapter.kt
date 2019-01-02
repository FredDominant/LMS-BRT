package com.noblemajesty.brt.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.noblemajesty.brt.R
import com.noblemajesty.brt.database.entities.BusSchedule

class BusScheduleAdapter(private val schedules: ArrayList<BusSchedule>): RecyclerView.Adapter<BusScheduleViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusScheduleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.partial_bus_schedules, parent, false)
        return BusScheduleViewHolder(view)
    }

    override fun getItemCount() = schedules.size

    override fun onBindViewHolder(holder: BusScheduleViewHolder, position: Int) {
        holder.setValues(schedules[position])
    }
}