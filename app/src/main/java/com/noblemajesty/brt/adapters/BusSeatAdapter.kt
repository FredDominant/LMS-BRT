package com.noblemajesty.brt.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.noblemajesty.brt.BusSeat
import com.noblemajesty.brt.R

class BusSeatAdapter(private val busSeats: ArrayList<BusSeat>): RecyclerView.Adapter<BusSeatViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusSeatViewHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.partial_seat, parent, false)
        return BusSeatViewHolder(view)
    }

    override fun getItemCount() = busSeats.size

    override fun onBindViewHolder(holder: BusSeatViewHolder, position: Int) {
        holder.setValues(busSeats[position])
    }
}