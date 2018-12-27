package com.noblemajesty.brt.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.noblemajesty.brt.R
import com.noblemajesty.brt.database.entities.Bus

class BusAdapter(private val listOfBuses: List<Bus>): RecyclerView.Adapter<BusViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.partial_bus, parent, false)
        return BusViewHolder(view)
    }

    override fun getItemCount() = listOfBuses.size

    override fun onBindViewHolder(holder: BusViewHolder, position: Int) {
        holder.setValues(listOfBuses[position])
    }
}