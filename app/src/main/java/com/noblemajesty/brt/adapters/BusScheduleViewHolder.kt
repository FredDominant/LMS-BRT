package com.noblemajesty.brt.adapters

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.noblemajesty.brt.R
import com.noblemajesty.brt.database.entities.BusSchedule
import com.noblemajesty.brt.views.home.MainActivity
import com.noblemajesty.brt.views.home.TripDetailFragment

class BusScheduleViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
    fun setValues(busSchedule: BusSchedule) {

        val busName = busSchedule.busName
        val cost = busSchedule.cost
        val departureDate = busSchedule.date
        val destination = busSchedule.destination
        val from = busSchedule.from
        val status = busSchedule.status
        val userId = busSchedule.userId
        val seatNumber = busSchedule.seatNumber
        val scheduleId = busSchedule.scheduleId

        view.findViewById<TextView>(R.id.scheduleBus).text = busName
        view.findViewById<TextView>(R.id.scheduleCost).text = cost
        view.findViewById<TextView>(R.id.scheduleDepartureDate).text = departureDate
        view.findViewById<TextView>(R.id.scheduleDestination).text = destination
        view.findViewById<TextView>(R.id.scheduleFrom).text = from
        view.findViewById<TextView>(R.id.scheduleStatus).text = status

        view.setOnClickListener { _ ->
            val bundle = Bundle().apply {
                putInt("userId", userId)
                putString("tripFrom", from)
                putString("tripDestination", destination)
                putString("departure", departureDate)
                putInt("seatNumber", seatNumber)
                putString("cost", cost)
                putString("status", status)
                putString("busName", busName)
                putInt("scheduleId", scheduleId!!)
            }
            val activity = view.context as? MainActivity
            activity?.goToFragment(TripDetailFragment.newInstance(), bundle)
        }
    }
}