package com.noblemajesty.brt.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.noblemajesty.brt.R
import com.noblemajesty.brt.database.entities.Bus

class BusViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
    fun setValues(bus: Bus) {
        view.findViewById<TextView>(R.id.busName).text = bus.name
        view.findViewById<TextView>(R.id.busColor).text = bus.color
        view.findViewById<TextView>(R.id.busCapacity).text = bus.capacity.toString()
    }
}
